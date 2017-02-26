package com.rossotti.basketball.integration;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.jpa.model.Game;
import java.time.LocalDate;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class GameFinderActivator {
	private final GameAppService gameAppService;
	private final Logger logger = LoggerFactory.getLogger(GameFinderActivator.class);

	@Autowired
	public GameFinderActivator(GameAppService gameAppService) {
		this.gameAppService = gameAppService;
	}

	@ServiceActivator(inputChannel = "inputChannel", outputChannel = "gameFinderChannel")
	public List<Game> processGames(ServiceProperties properties) {
		List<Game> games = new ArrayList<>();
		LocalDate gameDate = DateTimeConverter.getLocalDate(properties.getGameDate());
		if (properties.getGameTeam() == null || properties.getGameTeam().isEmpty()) {
			games = gameAppService.findByAsOfDate(gameDate);
			logger.info("findByDate: " + DateTimeConverter.getStringDate(gameDate));
		}
		else {
			Game game = gameAppService.findByTeamKeyAsOfDate(properties.getGameTeam(), gameDate);
			if (game.isFound()) {
				games.add(game);
			}
			logger.info("findByDateTeam: " + DateTimeConverter.getStringDate(gameDate) + " - " + properties.getGameTeam());
		}
		return games;
	}
}