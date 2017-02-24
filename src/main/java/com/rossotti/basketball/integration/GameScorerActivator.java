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
public class GameScorerActivator {
	private final GameBusService gameBusService;
	private final Logger logger = LoggerFactory.getLogger(GameScorerActivator.class);

	@Autowired
	public GameScorerActivator(GameBusService gameBusService) {
		this.gameBusService = gameBusService;
	}

	@ServiceActivator(inputChannel = "gameScoreChannel", outputChannel = "gameResultsChannel")
	public GameBusiness scoreGame(Game game) {
		GameBusiness gameBusiness = gameBusService.scoreGame(game);
		logger.info("gameScorer: route to gameResultsChannel");
		return gameBusiness;
	}
}