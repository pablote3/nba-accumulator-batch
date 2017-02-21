package com.rossotti.basketball.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@ComponentScan(basePackages = {"com.rossotti.basketball"})
@IntegrationComponentScan(basePackages = {"com.rossotti.basketball.integration"})

public class IntegrationConfig {
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel gameFinderChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel gameSplitterChannel() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel gameRouterChannel() {
        return new QueueChannel(20);
    }

    @Bean
    public MessageChannel gameScoreChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel gameResultsChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel gameAggregatorChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setMaxMessagesPerPoll(1L);
 //       pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }
}
