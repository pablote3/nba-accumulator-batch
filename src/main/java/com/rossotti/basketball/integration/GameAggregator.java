package com.rossotti.basketball.integration;

import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness.StatusCode;
import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class GameAggregator {
	private final Logger logger = LoggerFactory.getLogger(GameAggregator.class);

	@Aggregator(inputChannel = "gameAggregatorChannel", outputChannel = "outputChannel")
	public List<Game> aggregate(Collection<Message<?>> games) {
		logger.debug("begin gameAggregator");
		List<Game> gameList = new ArrayList<>();
		for (Message<?> msg : games) {
			logger.debug("msg.correlationId = " + msg.getHeaders().get("correlationId"));
			logger.debug("msg.sequenceNumber = " + msg.getHeaders().get("sequenceNumber"));
			logger.debug("msg.sequenceSize = " + msg.getHeaders().get("sequenceSize"));			
			GameBusiness gameBusiness = (GameBusiness)msg.getPayload();
			if (gameBusiness.getStatusCode().equals(StatusCode.ServerError) || gameBusiness.getStatusCode().equals(StatusCode.ClientError)) {
				gameList.add(null);
			}
			else {
				logger.info(msg.getHeaders().get("sequenceNumber") + " of " +
					msg.getHeaders().get("sequenceSize") + "  " +
					gameBusiness.getGame().getBoxScoreAway().getTeam().getAbbr() + " at " +
					gameBusiness.getGame().getBoxScoreHome().getTeam().getAbbr() + ": " +
					gameBusiness.getStatusCode()
				);
				gameList.add(gameBusiness.getGame());
			}
		}
		logger.debug("end gameAggregator");
		return gameList;
	}
}