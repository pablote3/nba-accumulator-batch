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

public class GameProcessor implements ItemProcessor<GameReaderInput, GameReaderInput> {

	@Autowired
	private GameAppService gameAppService;

	private final Logger logger = LoggerFactory.getLogger(GameProcessor.class);

	@Override
	public GameReaderInput process(GameReaderInput gameReaderInput) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		Game game = gameAppService.findByTeamKeyAsOfDate(gameReaderInput.getTeamKey(), LocalDate.parse(gameReaderInput.getGameDateTime(), formatter));

		return gameReaderInput;
	}

}
