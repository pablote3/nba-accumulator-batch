package com.rossotti.basketball.business.service;

import com.rossotti.basketball.app.service.StandingAppService;
import com.rossotti.basketball.business.model.StandingsBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness.StatusCode;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.app.model.StandingRecord;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.util.function.DateTimeConverter;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import com.rossotti.basketball.util.service.exception.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StandingBusService {
	private final PropertyService propertyService;

	private final RestStatsService restStatsService;

	private final FileStatsService fileStatsService;

	private final StandingAppService standingAppService;

	private final Logger logger = LoggerFactory.getLogger(StandingBusService.class);

	@Autowired
	public StandingBusService(FileStatsService fileStatsService, RestStatsService restStatsService, PropertyService propertyService, StandingAppService standingAppService) {
		this.fileStatsService = fileStatsService;
		this.restStatsService = restStatsService;
		this.propertyService = propertyService;
		this.standingAppService = standingAppService;
	}

	public StandingsBusiness rankStandings(String asOfDateString) {
		StandingsBusiness standingsBusiness = new StandingsBusiness();
		try {
			StandingsDTO standingsDTO;
			ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.standings");
			LocalDate asOfDate = DateTimeConverter.getLocalDate(asOfDateString);
			String event = DateTimeConverter.getStringDateNaked(asOfDate);
			if (clientSource == ClientSource.File) {
				standingsDTO = fileStatsService.retrieveStandings(event);
			}
			else if (clientSource == ClientSource.Api) {
				standingsDTO = restStatsService.retrieveStandings(event, false);
			}
			else {
				throw new PropertyException("Unknown");
			}

			if (standingsDTO.isFound()) {
				if (standingsDTO.standing.length > 0) {
					logger.debug("Rank standings");

					//clear existing standings
					standingAppService.deleteStandings(asOfDate);

					List<Standing> standings = standingAppService.getStandings(standingsDTO);
					Map<String, StandingRecord> standingsMap = standingAppService.buildStandingsMap(standings, asOfDate);

					for (Standing standing : standings) {
						String teamKey = standing.getTeam().getTeamKey();
						Map<String, StandingRecord> headToHeadMap = standingAppService.buildHeadToHeadMap(teamKey, asOfDate, standingsMap);
						StandingRecord standingRecord = standingAppService.calculateStrengthOfSchedule(teamKey, asOfDate, standingsMap, headToHeadMap);
						standing.setOpptGamesWon(standingRecord.getGamesWon());
						standing.setOpptGamesPlayed(standingRecord.getGamesPlayed());
						standing.setOpptOpptGamesWon(standingRecord.getOpptGamesWon());
						standing.setOpptOpptGamesPlayed(standingRecord.getOpptGamesPlayed());
						Standing createdStanding = standingAppService.createStanding(standing);
						if (createdStanding.isCreated()) {
							BigDecimal opponentRecord = standingRecord.getGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getGamesWon()).divide(new BigDecimal(standingRecord.getGamesPlayed()), 4, RoundingMode.HALF_UP);
							BigDecimal opponentOpponentRecord = standingRecord.getOpptGamesPlayed() == 0 ? new BigDecimal(0) : new BigDecimal(standingRecord.getOpptGamesWon()).divide(new BigDecimal(standingRecord.getOpptGamesPlayed()), 4, RoundingMode.HALF_UP);
							logger.debug("    Opponent Games Won/Played = " + standingRecord.getGamesWon() + "-" + standingRecord.getGamesPlayed());
							logger.debug("    OpptOppt Games Won/Played = " + standingRecord.getOpptGamesWon() + "-" + standingRecord.getOpptGamesPlayed());
							logger.debug("    Opponent Record = " + opponentRecord);
							logger.debug("    OpptOppt Record = " + opponentOpponentRecord);
							logger.info("  Strenghth Of Schedule  " + standing.getTeam().getAbbr() + ": " + opponentRecord.multiply(new BigDecimal(2)).add(opponentOpponentRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));
						} else {
							logger.info("Unable to create standing");
							throw new Exception("Unknown");
						}
					}
					standingsBusiness.setStandings(standingAppService.findStandings(asOfDate));
					logger.info("StandingsCount: " + standings.size() + " Completed: route to outputChannel");
					standingsBusiness.setStatusCode(StatusCode.Completed);
				}
				else {
					logger.info("Client exception - standings found with empty list");
					standingsBusiness.setStatusCode(StatusCode.ClientError);
				}
			}
			else if (standingsDTO.isNotFound()) {
				logger.info("Unable to find standings");
				standingsBusiness.setStatusCode(StatusCode.ClientError);
			}
			else {
				logger.info("Client error retrieving standings");
				standingsBusiness.setStatusCode(StatusCode.ClientError);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Team.class)) {
				logger.info("Team not found");
			}
			standingsBusiness.setStatusCode(StatusCode.ClientError);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			standingsBusiness.setStatusCode(StatusCode.ServerError);
		}
		catch (Exception e) {
			logger.info("Unexpected exception = " + e);
			standingsBusiness.setStatusCode(StatusCode.ServerError);
		}
		return standingsBusiness;
	}
}