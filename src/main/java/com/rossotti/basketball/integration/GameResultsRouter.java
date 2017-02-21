package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;

@Configuration
public class GameResultsRouter {
	private final Logger logger = LoggerFactory.getLogger(GameResultsRouter.class);

	@Router(inputChannel = "gameResultsChannel")
	public String routeGame(GameBusiness appGame) {
		if (appGame.isRosterUpdate()) {
			logger.info("Game " + appGame.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " + 
				appGame.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				appGame.getStatusCode() +
				": route to gameRouterChannel"
			);
			return "gameRouterChannel";
		}
		else {
			logger.info("Game " + appGame.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " + 
				appGame.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				appGame.getStatusCode() +
				": route to gameAggregatorChannel"
			);			
			return "gameAggregatorChannel";
		}
	}
}