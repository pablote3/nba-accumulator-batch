package com.rossotti.basketball.app.service;

import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.service.GameJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameAppService {
	private final GameJpaService gameJpaService;

	@Autowired
	public GameAppService(GameJpaService gameJpaService) {
		this.gameJpaService = gameJpaService;
	}

	public List<Game> findByAsOfDate(LocalDate asOfDate) {
		return gameJpaService.findByAsOfDate(asOfDate);
	}

	public Game findByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate) {
		return gameJpaService.findByTeamKeyAndAsOfDate(teamKey, asOfDate);
	}

	public int findCountByAsOfDate(LocalDate asOfDate) {
		return gameJpaService.findCountByAsOfDate(asOfDate);
	}

	public LocalDateTime findPreviousByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate) {
		return gameJpaService.findPreviousByTeamKeyAsOfDate(teamKey, asOfDate);
	}

	public List<Game> findByTeamKeyAsOfDateSeason(String teamKey, LocalDate gameDate) {
		return gameJpaService.findByTeamKeyAndAsOfDateSeason(teamKey, gameDate);
	}

	public Game updateGame(Game game) {
		return gameJpaService.update(game);
	}

	public Game createGame(Game game) {
		return gameJpaService.create(game);
	}
}