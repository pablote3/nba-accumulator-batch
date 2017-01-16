package com.rossotti.basketball.batch;

import com.rossotti.basketball.batch.exception.RosterRebuildException;
import com.rossotti.basketball.batch.exception.SkipStepException;
import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.model.RosterPlayerBusiness;
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

	@Override
	public Game process(Game game) throws Exception {
		String gameDate = DateTimeConverter.getStringDate(game.getGameDateTime());
		logger.info("GameProcessor gameDate: " + gameDate + " awayTeam: " + game.getBoxScoreAway().getTeam().getAbbr() + " homeTeam: " + game.getBoxScoreHome().getTeam().getAbbr());
		GameBusiness gameBusiness = gameBusService.scoreGame(game);
		if (gameBusiness.isRosterUpdate()) {
			RosterPlayerBusiness rosterPlayerBusiness = rosterPlayerBusService.loadRoster(gameDate, gameBusiness.getRosterLastTeam());
			if (rosterPlayerBusiness.isCompleted()) {
				throw new RosterRebuildException("RosterLoad complete - gameDate: " + gameDate + " team: " + gameBusiness.getRosterLastTeam());
			}
			else {
				throw new SkipStepException("RosterLoad problem - status code: " + rosterPlayerBusiness.getStatusCode());
			}
		}
		else if (gameBusiness.isCompleted()) {
			logger.info("GameScore complete " + DateTimeConverter.getStringDate(game.getGameDateTime()));
			return game;
		}
		else {
			throw new SkipStepException("GameScore problem - status code: " + gameBusiness.getStatusCode());
		}
	}
}