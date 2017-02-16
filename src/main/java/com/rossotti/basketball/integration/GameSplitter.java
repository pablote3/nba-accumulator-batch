package com.rossotti.basketball.integration;

import com.rossotti.basketball.jpa.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.*;

public class GameSplitter extends AbstractMessageSplitter {
	private final Logger logger = LoggerFactory.getLogger(GameSplitter.class);
	private final Map<String, List<Message<?>>> splittedMessages = new HashMap<String, List<Message<?>>>();

	@Override
	protected Object splitMessage(Message<?> message) {
		Collection<Message<?>> messages = new ArrayList<Message<?>>();
		List<Game> games = (List<Game>) message.getPayload();
		for (int i = 0; i < games.size(); i++) {
			Game game = games.get(i);
			Message<?> msg = MessageBuilder
				.withPayload(game)
				.setReplyChannel((MessageChannel)message.getHeaders().getReplyChannel())
				.setCorrelationId(game.getGameDateTime())
				.setSequenceNumber(i)
				.setSequenceSize(games.size())
				.build();
			messages.add(msg);
			addMessage(""+ games.size(), msg);
		}
		logger.info("gameCount: " + games.size());
		return messages;
	}

	private Map<String, List<Message<?>>> getSplittedMessages() {
		return this.splittedMessages;
	}

	private List<Message<?>> getSplittedMessagesByKey(String key) {
		if (!getSplittedMessages().containsKey(key)) {
			addListOfSplittedMessages(key);
		}
		return getSplittedMessages().get(key);
	}

	private void addMessage(String key, Message<?> message) {
		getSplittedMessagesByKey(key).add(message);
	}

	private void addListOfSplittedMessages(String key) {
		getSplittedMessages().put(key, (new ArrayList<Message<?>>()));
	}
}