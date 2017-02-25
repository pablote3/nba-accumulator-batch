package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.StandingsBusiness;
import com.rossotti.basketball.business.service.StandingBusService;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

@Configuration
public class StandingsRankerActivator {
	private final StandingBusService standingBusService;
	private final Logger logger = LoggerFactory.getLogger(StandingsRankerActivator.class);

	@Autowired
	public StandingsRankerActivator(StandingBusService standingBusService) {
		this.standingBusService = standingBusService;
	}

	@ServiceActivator(inputChannel = "standingsRankChannel", outputChannel = "outputChannel")
	public List<Game> rankStandings(List<Game> games) {
		StandingsBusiness standingBusiness = standingBusService.rankStandings(DateTimeConverter.getStringDate(games.get(0).getGameDateTime()));
		logger.info("standingsRanker: standingsCount: " + standingBusiness.getStandings().size() + " Completed: route to outputChannel");
		return games;
	}
}