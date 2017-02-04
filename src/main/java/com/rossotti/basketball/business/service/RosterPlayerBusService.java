package com.rossotti.basketball.business.service;

import com.rossotti.basketball.app.service.PlayerAppService;
import com.rossotti.basketball.app.service.RosterPlayerAppService;
import com.rossotti.basketball.business.model.RosterPlayerBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness.StatusCode;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.Player;
import com.rossotti.basketball.jpa.model.RosterPlayer;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.util.function.DateTimeConverter;
import com.rossotti.basketball.util.function.FormatString;
import com.rossotti.basketball.util.function.ThreadSleep;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import com.rossotti.basketball.util.service.exception.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RosterPlayerBusService {
	private final PropertyService propertyService;

	private final RestStatsService restStatsService;

	private final FileStatsService fileStatsService;

	private final RosterPlayerAppService rosterPlayerAppService;

	private final PlayerAppService playerAppService;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerBusService.class);

	@Autowired
	public RosterPlayerBusService(RosterPlayerAppService rosterPlayerAppService, PropertyService propertyService, FileStatsService fileStatsService, RestStatsService restStatsService, PlayerAppService playerAppService) {
		this.propertyService = propertyService;
		this.fileStatsService = fileStatsService;
		this.restStatsService = restStatsService;
		this.playerAppService = playerAppService;
		this.rosterPlayerAppService = rosterPlayerAppService;
	}

	public RosterPlayerBusiness loadRoster(String asOfDateString, String teamKey) {
		RosterPlayerBusiness rosterPlayerBusiness = new RosterPlayerBusiness();
		try {
			RosterDTO rosterDTO;
			LocalDate fromDate = DateTimeConverter.getLocalDate(asOfDateString);
			LocalDate toDate = DateTimeConverter.getLocalDateSeasonMax(fromDate);
			PropertyService.ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.roster");
			if (clientSource == ClientSource.File) {
				rosterDTO = fileStatsService.retrieveRoster(teamKey, fromDate);
			}
			else if (clientSource == ClientSource.Api) {
				ThreadSleep.sleep(propertyService.getProperty_Int("sleep.duration"));
				rosterDTO = restStatsService.retrieveRoster(teamKey, true, fromDate);
			}
			else {
				throw new PropertyException("Unknown");
			}

			if (rosterDTO.isFound()) {
				if (rosterDTO.players.length > 0) {
					//activate new roster players
					logger.info("Activate new roster players");
					List<RosterPlayer> activeRosterPlayers = rosterPlayerAppService.getRosterPlayers(rosterDTO.players, fromDate, teamKey);
					if (activeRosterPlayers.size() > 0) {
						for (RosterPlayer activeRosterPlayer : activeRosterPlayers) {
							Player activePlayer = activeRosterPlayer.getPlayer();
							RosterPlayer finderRosterPlayer = rosterPlayerAppService.findByPlayerNameTeamAsOfDate(activePlayer.getLastName(), activePlayer.getFirstName(), teamKey, fromDate);
							if (finderRosterPlayer.isNotFound()) {
								//player is not on current team roster
								finderRosterPlayer = rosterPlayerAppService.findByPlayerNameBirthdateAsOfDate(activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate(), fromDate);
								if (finderRosterPlayer.isNotFound()) {
									//player is not active on any roster
									Player finderPlayer = playerAppService.findByPlayerNameBirthdate(activePlayer.getLastName(), activePlayer.getFirstName(), activePlayer.getBirthdate());
									if (finderPlayer.isNotFound()) {
										//player does not exist
										Player createPlayer = playerAppService.createPlayer(activePlayer);
										activeRosterPlayer.setPlayer(createPlayer);
										activeRosterPlayer.setFromDate(fromDate);
										activeRosterPlayer.setToDate(toDate);
										logger.info(generateLogMessage("Player does not exist", activeRosterPlayer));
										rosterPlayerAppService.createRosterPlayer(activeRosterPlayer);
									} else {
										//player does exist, not on any roster
										activeRosterPlayer.setPlayer(finderPlayer);
										activeRosterPlayer.setFromDate(fromDate);
										activeRosterPlayer.setToDate(toDate);
										logger.info(generateLogMessage("Player does exist, not on any roster", activeRosterPlayer));
										rosterPlayerAppService.createRosterPlayer(activeRosterPlayer);
									}
								} else {
									//player is on another roster for current season
									finderRosterPlayer.setToDate(DateTimeConverter.getDateMinusOneDay(fromDate));
									logger.info(generateLogMessage("Player on another team - Terminate", finderRosterPlayer));
									rosterPlayerAppService.updateRosterPlayer(finderRosterPlayer);
									activeRosterPlayer.setFromDate(fromDate);
									activeRosterPlayer.setToDate(toDate);
									activeRosterPlayer.getPlayer().setId(finderRosterPlayer.getPlayer().getId());
									logger.info(generateLogMessage("Player on another team - Add", activeRosterPlayer));
									rosterPlayerAppService.createRosterPlayer(activeRosterPlayer);
								}
							} else {
								//player is on current team roster
								activeRosterPlayer.setFromDate(finderRosterPlayer.getFromDate());
								activeRosterPlayer.setToDate(finderRosterPlayer.getToDate());
								logger.debug(generateLogMessage("Player on current team roster", activeRosterPlayer));
							}
						}

						//deactivate terminated roster players
						logger.info("Deactivate terminated roster players");
						List<RosterPlayer> priorRosterPlayers = rosterPlayerAppService.findByTeamKeyAsOfDate(fromDate, teamKey);
						if (priorRosterPlayers.size() > 0) {
							boolean foundPlayerOnRoster;
							for (RosterPlayer priorRosterPlayer : priorRosterPlayers) {
								Player priorPlayer = priorRosterPlayer.getPlayer();
								foundPlayerOnRoster = false;
								for (RosterPlayer activeRosterPlayer : activeRosterPlayers) {
									Player activePlayer = activeRosterPlayer.getPlayer();
									if (priorPlayer.getLastName().equals(activePlayer.getLastName()) &&
											priorPlayer.getFirstName().equals(activePlayer.getFirstName()) &&
											priorPlayer.getBirthdate().equals(activePlayer.getBirthdate())) {
										//player is on current team roster
										logger.debug(generateLogMessage("Player on current team roster", priorRosterPlayer));
										foundPlayerOnRoster = true;
										break;
									}
								}
								if (!foundPlayerOnRoster) {
									//player is not on current team roster
									priorRosterPlayer.setToDate(DateTimeConverter.getDateMinusOneDay(fromDate));
									logger.info(generateLogMessage("Player is not on current team roster", priorRosterPlayer));
									rosterPlayerAppService.updateRosterPlayer(priorRosterPlayer);
								}
							}
							rosterPlayerBusiness.setRosterPlayers(rosterPlayerAppService.findByTeamKeyAsOfDate(fromDate, teamKey));
							rosterPlayerBusiness.setStatusCode(StatusCode.Completed);
						}
						else {
							logger.info("Unable to find roster players on deactivation");
							rosterPlayerBusiness.setStatusCode(StatusCode.ServerError);
						}
					}
					else {
						logger.info("Unable to get roster players on activation");
						rosterPlayerBusiness.setStatusCode(StatusCode.ServerError);
					}
				}
				else {
					logger.info("Client exception - roster found with empty player list");
					rosterPlayerBusiness.setStatusCode(StatusCode.ClientError);
				}
			}
			else if (rosterDTO.isNotFound()) {
				logger.info("Unable to find roster");
				rosterPlayerBusiness.setStatusCode(StatusCode.ClientError);
			}
			else if (rosterDTO.isClientException()) {
				logger.info("Client exception");
				rosterPlayerBusiness.setStatusCode(StatusCode.ClientError);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Team.class)) {
				logger.info("Team not found");
			}
			rosterPlayerBusiness.setStatusCode(StatusCode.ClientError);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			rosterPlayerBusiness.setStatusCode(StatusCode.ServerError);
		}
		catch (Exception e) {
			logger.info("Unexpected exception = " + e);
			rosterPlayerBusiness.setStatusCode(StatusCode.ServerError);
		}
		return rosterPlayerBusiness;
	}

	private String generateLogMessage(String messageType, RosterPlayer rosterPlayer) {
		return FormatString.padString(messageType, 40) +
				" fromDate = " + DateTimeConverter.getStringDate(rosterPlayer.getFromDate()) +
				" toDate = " + DateTimeConverter.getStringDate(rosterPlayer.getToDate()) +
				" dob = " + DateTimeConverter.getStringDate(rosterPlayer.getPlayer().getBirthdate()) +
				" name = " + FormatString.padString(rosterPlayer.getPlayer().getFirstName() + " " + rosterPlayer.getPlayer().getLastName(), 35);
	}
}
