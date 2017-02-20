package com.rossotti.basketball.integration;

import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;

import java.util.List;

@Configuration
public class GameFinderRouter {
	private final Logger logger = LoggerFactory.getLogger(GameFinderRouter.class);

	@Router(inputChannel = "gameFinderChannel")
	public String routeGame(List<Game> games) {
		if (games.size() > 0) {
			logger.info("gameCount: " + games.size() + ": route to gameSplitterChannel");
			return "gameSplitterChannel";
		}
		else {
			logger.info("gameCount: " + games.size() + ": route to outputChannel");			
			return "outputChannel";
		}
	}
}