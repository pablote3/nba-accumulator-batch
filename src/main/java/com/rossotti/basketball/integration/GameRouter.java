package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;

@Configuration
public class GameRouter {
	private final Logger logger = LoggerFactory.getLogger(GameRouter.class);

	@Router(inputChannel = "gameRouterChannel")
	public String routeGame(GameBusiness gameBusiness) {
		if(gameBusiness.isInitial() || gameBusiness.isRosterComplete()) {
			logger.info("GameBusiness " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
					gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
					gameBusiness.getStatusCode() +
					": route to gameScoreChannel"
			);
			return "gameScoreChannel";
		}
		else if (gameBusiness.isRosterUpdate()) {
			logger.info("GameBusiness " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				gameBusiness.getStatusCode() +
				": route to rosterLoadChannel"
			);
			return "rosterLoadChannel";
		}
		else {
			logger.info("GameBusiness " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
				gameBusiness.getStatusCode() +
				": route to gameAggregatorChannel"
			);
			return "gameAggregatorChannel";
		}
	}
}