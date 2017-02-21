package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.service.GameBusService;
import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;

@Configuration
public class GameScorer {
	private final GameBusService gameBusService;
	private final Logger logger = LoggerFactory.getLogger(GameScorer.class);

	@Autowired
	public GameScorer(GameBusService gameBusService) {
		this.gameBusService = gameBusService;
	}

	@ServiceActivator(inputChannel = "gameScoreChannel", outputChannel = "gameResultsChannel")
	public GameBusiness scoreGame(Game game) {
		GameBusiness gameBusiness = gameBusService.scoreGame(game);
		logger.info("gameScorer: " + game.getGameDateTime());
		return gameBusiness;
	}
}