package com.rossotti.basketball.integration;

import com.rossotti.basketball.jpa.model.Game;
import org.springframework.integration.annotation.MessagingGateway;
import java.util.List;

@MessagingGateway(defaultRequestChannel = "inputChannel", defaultReplyChannel = "outputChannel")
public interface GatewayService {
	List<Game> processGames(ServiceProperties serviceProperties);
}