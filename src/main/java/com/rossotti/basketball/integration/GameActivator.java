package com.rossotti.basketball.integration;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.jpa.model.Game;
import java.time.LocalDate;

import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GameActivator {
	private final GameAppService gameAppService;
	private final Logger logger = LoggerFactory.getLogger(GameActivator.class);

	@Autowired
	public GameActivator(GameAppService gameAppService) {
		this.gameAppService = gameAppService;
	}

	public List<Game> processGames(ServiceProperties properties) {
		List<Game> games = new ArrayList<Game>();
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