package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;

@Configuration
public class GameRouter {
	private final Logger logger = LoggerFactory.getLogger(GameRouter.class);

	@Router(inputChannel = "gameRouterChannel")
	public String routeGame(Game game) {
		logger.info("Game " + game.getBoxScoreAway().getTeam().getAbbr() + " at " + 
			game.getBoxScoreHome().getTeam().getAbbr() + " " +
			game.getStatus() +
			": route to gameScoreChannel"
		);
		return "gameScoreChannel";
	}

//	public String routeGame(GameBusiness gameBusiness) {
//		if (gameBusiness.isRosterUpdate()) {
//			logger.info("AppGame " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
//				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
//				gameBusiness.getStatusCode() +
//				": route to rosterLoadChannel"
//			);
//			return "rosterLoadChannel";
//		}
//		else if(gameBusiness.isRosterComplete()) {
//			logger.info("AppGame " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
//				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
//				gameBusiness.getStatusCode() +
//				": route to gameScoreChannel"
//			);
//			return "gameScoreChannel";
//		}
//		else {
//			logger.info("AppGame " + gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
//				gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + " " +
//				gameBusiness.getStatusCode() +
//				": route to gameAggregatorChannel"
//			);
//			return "gameAggregatorChannel";
//		}
//	}
}