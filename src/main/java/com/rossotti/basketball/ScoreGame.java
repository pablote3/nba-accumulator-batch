package com.rossotti.basketball;

import com.rossotti.basketball.integration.GatewayService;
import com.rossotti.basketball.integration.ServiceProperties;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@IntegrationComponentScan
public class ScoreGame {
	private final Logger logger = LoggerFactory.getLogger(ScoreGame.class);

	public static void main(String[] args) {
		ServiceProperties serviceProperties = new ServiceProperties();

		if (System.getProperty("gameDate") != null) {
			String gameDate = System.getProperty("gameDate");
			if (gameDate.isEmpty()) {
				serviceProperties.setGameDate(DateTimeConverter.getStringDate(LocalDate.now().minusDays(1)));
			}
			else {
				if (DateTimeConverter.isDate(gameDate)) {
					serviceProperties.setGameDate(gameDate);
				}
				else {
					System.out.println("Invalid gameDate argument");
					System.exit(1);
				}
			}
		}
		else {
			System.out.println("Need to supply gameDate argument");
			System.exit(1);
		}

		if (System.getProperty("gameTeam") != null) {
			String gameTeam =  System.getProperty("gameTeam");
			if (gameTeam.isEmpty()) {
				serviceProperties.setGameTeam("");
			}
			else {
				serviceProperties.setGameTeam(gameTeam);
			}
		}
		else {
			System.out.println("Need to supply gameTeam argument");
			System.exit(1);
		}

		if (serviceProperties.getGameTeam().isEmpty()) {
			System.out.println("\n" + "begin gatewayService for gameDate = " + serviceProperties.getGameDate());
		}
		else {
			System.out.println("\n" + "begin gatewayService for gameDate = " + serviceProperties.getGameDate() + " and gameTeam = " + serviceProperties.getGameTeam());
		}

		ConfigurableApplicationContext ctx = SpringApplication.run(ScoreGame.class, args);
		GatewayService gatewayService = ctx.getBean(GatewayService.class);
		List<Game> games = gatewayService.processGames(serviceProperties);

		if (games != null && games.size() > 0) {
			System.out.println("end gatewayService, processed " + games.size() + " games" + "\n");
		}
		else {
			System.out.println("end gatewayService, no games processed");
		}
	}
}