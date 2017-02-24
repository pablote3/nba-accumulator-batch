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
	public String routeGame(GameBusiness gameBusiness) {
		if (gameBusiness.isRosterUpdate()) {
			logger.info("Game " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				gameBusiness.getStatusCode() +
				": route to gameRouterChannel"
			);
			return "gameRouterChannel";
		}
		else {
			logger.info("Game " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				gameBusiness.getStatusCode() +
				": route to gameAggregatorChannel"
			);			
			return "gameAggregatorChannel";
		}
	}
}