package com.rossotti.basketball.batch;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.batch.model.GameReaderInput;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GameProcessor implements ItemProcessor<Game, Game> {

	@Autowired
	private GameAppService gameAppService;

	private final Logger logger = LoggerFactory.getLogger(GameProcessor.class);

	@Override
	public Game process(Game game) throws Exception {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//		LocalDate gameDate = LocalDate.parse(game.getGameDateTime(), formatter);
//		logger.info("Game finder " + gameDate);
//		Game game = gameAppService.findByTeamKeyAsOfDate(game.getTeamKey(), gameDate);
		logger.info("Updating Game Status " + DateTimeConverter.getStringDate(game.getGameDateTime()));
		logger.info("Game Away Team = " + game.getBoxScoreAway().getTeam().getAbbr());
		game.setStatus(Game.GameStatus.Completed);
		return game;
	}
}