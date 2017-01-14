package com.rossotti.basketball.batch;

import com.rossotti.basketball.business.model.GameBusiness;
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
		logger.info("Updating Game Status " + DateTimeConverter.getStringDate(game.getGameDateTime()));
		GameBusiness gameBusiness = gameBusService.scoreGame(game);
		if (gameBusiness.isRosterUpdate()) {
//			rosterPlayerBusService.loadRoster()
		}
		return game;
	}
}