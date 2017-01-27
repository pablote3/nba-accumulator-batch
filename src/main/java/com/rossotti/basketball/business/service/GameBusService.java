package com.rossotti.basketball.business.service;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.app.service.OfficialAppService;
import com.rossotti.basketball.app.service.RosterPlayerAppService;
import com.rossotti.basketball.app.service.TeamAppService;
import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness.StatusCode;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.BoxScore.Result;
import com.rossotti.basketball.jpa.model.Game.GameStatus;
import com.rossotti.basketball.util.function.DateTimeConverter;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import com.rossotti.basketball.util.service.exception.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class GameBusService {
	private final PropertyService propertyService;

	private final RestStatsService restStatsService;

	private final FileStatsService fileStatsService;

	private final OfficialAppService officialAppService;

	private final RosterPlayerAppService rosterPlayerAppService;

	private final TeamAppService teamAppService;

	private final GameAppService gameAppService;

	private final Logger logger = LoggerFactory.getLogger(GameBusService.class);

	@Autowired
	public GameBusService(OfficialAppService officialAppService, RestStatsService restStatsService, TeamAppService teamAppService, RosterPlayerAppService rosterPlayerAppService, GameAppService gameAppService, PropertyService propertyService, FileStatsService fileStatsService) {
		this.officialAppService = officialAppService;
		this.restStatsService = restStatsService;
		this.teamAppService = teamAppService;
		this.rosterPlayerAppService = rosterPlayerAppService;
		this.gameAppService = gameAppService;
		this.propertyService = propertyService;
		this.fileStatsService = fileStatsService;
	}

	public GameBusiness scoreGame(Game game) {
		GameBusiness gameBusiness = new GameBusiness();
		try {
			BoxScore awayBoxScore = game.getBoxScoreAway();
			BoxScore homeBoxScore = game.getBoxScoreHome();
			String awayTeamKey = awayBoxScore.getTeam().getTeamKey();
			String homeTeamKey = homeBoxScore.getTeam().getTeamKey();
			LocalDateTime gameDateTime = game.getGameDateTime();
			LocalDate gameDate = DateTimeConverter.getLocalDate(gameDateTime);

			String event = DateTimeConverter.getStringDateNaked(gameDateTime) + "-" + awayTeamKey + "-at-" + homeTeamKey;

			if (game.isScheduled()) {
				logger.debug("Scheduled game ready to be scored: " + event);

				GameDTO gameDTO;
				ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.boxScore");
				if (clientSource == ClientSource.File) {
					gameDTO = fileStatsService.retrieveBoxScore(event);
				}
				else if (clientSource == ClientSource.Api) {
					gameDTO = restStatsService.retrieveBoxScore(event, false);
				}
				else {
					throw new PropertyException("Unknown");
				}

				if (gameDTO.isFound()) {
					awayBoxScore.updateTotals(gameDTO.away_totals);
					homeBoxScore.updateTotals(gameDTO.home_totals);
					awayBoxScore.updatePeriodScores(gameDTO.away_period_scores);
					homeBoxScore.updatePeriodScores(gameDTO.home_period_scores);
					gameBusiness.setRosterLastTeam(awayTeamKey);
					awayBoxScore.setBoxScorePlayers(rosterPlayerAppService.getBoxScorePlayers(gameDTO.away_stats, awayBoxScore, gameDate, awayTeamKey));
					gameBusiness.setRosterLastTeam(homeTeamKey);
					homeBoxScore.setBoxScorePlayers(rosterPlayerAppService.getBoxScorePlayers(gameDTO.home_stats, homeBoxScore, gameDate, homeTeamKey));
					game.setGameOfficials(officialAppService.getGameOfficials(gameDTO.officials, gameDate));
					awayBoxScore.setTeam(teamAppService.findTeamByTeamKey(awayTeamKey, gameDate));
					homeBoxScore.setTeam(teamAppService.findTeamByTeamKey(homeTeamKey, gameDate));

					if (gameDTO.away_totals.getPoints() > gameDTO.home_totals.getPoints()) {
						awayBoxScore.setResult(Result.Win);
						homeBoxScore.setResult(Result.Loss);
					}
					else {
						awayBoxScore.setResult(Result.Loss);
						homeBoxScore.setResult(Result.Win);
					}

					awayBoxScore.setDaysOff((short)DateTimeConverter.getDaysBetweenTwoDateTimes(gameAppService.findPreviousByTeamKeyAsOfDate(awayTeamKey, gameDate), gameDateTime));
					homeBoxScore.setDaysOff((short)DateTimeConverter.getDaysBetweenTwoDateTimes(gameAppService.findPreviousByTeamKeyAsOfDate(homeTeamKey, gameDate), gameDateTime));
					game.setStatus(GameStatus.Completed);
					gameBusiness.setStatusCode(StatusCode.Completed);
				}
				else if (gameDTO.isNotFound()) {
					logger.info("Unable to find game");
					gameBusiness.setStatusCode(StatusCode.ClientError);
				}
				else if (gameDTO.isClientException()) {
					logger.info("Client exception");
					gameBusiness.setStatusCode(StatusCode.ClientError);
				}
			}
			else {
				logger.info(game.getStatus() + " game not eligible to be scored: " + event);
				gameBusiness.setStatusCode(StatusCode.Completed);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Official.class)) {
				logger.info("Official not found - need to add official");
				gameBusiness.setStatusCode(StatusCode.OfficialError);
			}
			else if (nse.getEntityClass().equals(Team.class)) {
				logger.info("Team not found - need to add team");
				gameBusiness.setStatusCode(StatusCode.TeamError);
			}
			else if (nse.getEntityClass().equals(RosterPlayer.class)) {
				logger.info("RosterPlayer not found - need to rebuild active roster");
				gameBusiness.setStatusCode(StatusCode.RosterUpdate);
			}
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			gameBusiness.setStatusCode(StatusCode.ServerError);
		}
		catch (Exception e) {
			logger.info("Unexpected exception = " + e);
			gameBusiness.setStatusCode(StatusCode.ServerError);
		}
		finally {
			gameBusiness.setGame(game);
		}
		return gameBusiness;
	}
}
