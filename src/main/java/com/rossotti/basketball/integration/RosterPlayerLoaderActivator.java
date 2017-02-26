package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.service.RosterPlayerBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;

@Configuration
public class RosterPlayerLoaderActivator {
	private final RosterPlayerBusService rosterPlayerBusService;
	private final Logger logger = LoggerFactory.getLogger(RosterPlayerLoaderActivator.class);

	@Autowired
	public RosterPlayerLoaderActivator(RosterPlayerBusService rosterPlayerBusService) {
		this.rosterPlayerBusService = rosterPlayerBusService;
	}

	@ServiceActivator(inputChannel = "rosterLoadChannel", outputChannel = "gameRouterChannel")
	public GameBusiness loadRoster(GameBusiness gameBusiness) {
		gameBusiness = rosterPlayerBusService.loadRoster(gameBusiness);
		logger.info("rosterPlayerLoader: " + gameBusiness.getStatusCode() + " : route to gameRouterChannel");
		return gameBusiness;
	}
}