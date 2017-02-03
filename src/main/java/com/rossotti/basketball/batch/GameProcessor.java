package com.rossotti.basketball.batch;

import com.rossotti.basketball.batch.exception.SkipStepException;
import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.model.RosterPlayerBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness;
import com.rossotti.basketball.business.service.GameBusService;
import com.rossotti.basketball.business.service.RosterPlayerBusService;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class GameProcessor implements ItemProcessor<Game, Game> {

	@Autowired
	private GameBusService gameBusService;

	@Autowired
	private RosterPlayerBusService rosterPlayerBusService;

	private final Logger logger = LoggerFactory.getLogger(GameProcessor.class);
	private String rosterLastTeam;

	@Override
	public Game process(Game game) throws Exception {
		String gameDate = DateTimeConverter.getStringDate(game.getGameDateTime());
		logger.info("GameProcessor gameDate: " + gameDate + " awayTeam: " + game.getBoxScoreAway().getTeam().getAbbr() + " homeTeam: " + game.getBoxScoreHome().getTeam().getAbbr());
		GameBusiness gameBusiness = new GameBusiness(game);
		gameBusiness = this.scoreGame(gameBusiness);
		if (gameBusiness.isRosterComplete()) {
			gameBusiness = this.scoreGame(gameBusiness);
			if (gameBusiness.isRosterComplete()) {
				gameBusiness = this.scoreGame(gameBusiness);
			}
		}
		if (gameBusiness.isCompleted()) {
			logger.info("GameScore complete " + DateTimeConverter.getStringDate(game.getGameDateTime()));
			return game;
		}
		else {
			throw new SkipStepException("GameScore problem - status code: " + gameBusiness.getStatusCode());
		}
	}

	private GameBusiness scoreGame(GameBusiness gameBusiness) throws Exception {
		gameBusiness = gameBusService.scoreGame(gameBusiness.getGame());
		if (gameBusiness.isClientError() || gameBusiness.isServerError() || gameBusiness.isOfficialError() || gameBusiness.isTeamError()) {
			throw new SkipStepException("ScoreGame problem - status code: " + gameBusiness.getStatusCode());
		}
		else if (gameBusiness.isRosterUpdate()) {
			if (rosterLastTeam == null || !rosterLastTeam.equals(gameBusiness.getRosterLastTeam())) {
				rosterLastTeam = gameBusiness.getRosterLastTeam();
				RosterPlayerBusiness rosterPlayerBusiness = rosterPlayerBusService.loadRoster(DateTimeConverter.getStringDate(gameBusiness.getGame().getGameDateTime()), gameBusiness.getRosterLastTeam());
				if (rosterPlayerBusiness.isClientError() || rosterPlayerBusiness.isServerError()) {
					throw new SkipStepException("RosterLoad problem - status code: " + rosterPlayerBusiness.getStatusCode());
				}
				else if (rosterPlayerBusiness.isCompleted()) {
					gameBusiness.setStatusCode(StatusCodeBusiness.StatusCode.RosterComplete);
				}
			}
			else {
				throw new SkipStepException("Roster already updated for team : " + rosterLastTeam);
			}
		}
		return gameBusiness;
	}
}