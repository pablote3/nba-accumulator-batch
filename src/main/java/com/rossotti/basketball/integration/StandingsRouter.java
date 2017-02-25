package com.rossotti.basketball.integration;

import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import java.util.List;

@Configuration
public class StandingsRouter {
	private final Logger logger = LoggerFactory.getLogger(StandingsRouter.class);

	@Router(inputChannel = "standingsRouterChannel")
	public String routeStandings(List<Game> games) {
		if (games.size() > 0) {
			for (int i = 0; i < games.size(); i++) {
				Game game = games.get(i);
				if (game != null) {
					if (!game.isCompleted() && !game.isPostponed() && !game.isCancelled()) {
						logger.info("game " + i + " " + game.getStatus() + ": route to outputChannel");
						return "outputChannel";
					}
				}
				else {
					logger.info("game " + i + " null: route to outputChannel");
					return "outputChannel";
				}
			}
		}
		else {
			logger.info("no games completed: route to outputChannel");
			return "outputChannel";
		}
		logger.info("gameCount: " + games.size() + " Completed: route to standingsRankChannel");
		return "standingsRankChannel";
	}
}