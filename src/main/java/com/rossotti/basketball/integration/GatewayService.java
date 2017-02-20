package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.StandingsBusiness;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "inputChannel", defaultReplyChannel = "outputChannel")
public interface GatewayService {
	StandingsBusiness processGames(ServiceProperties serviceProperties);
}