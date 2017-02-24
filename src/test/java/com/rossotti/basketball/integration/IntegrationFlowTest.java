package com.rossotti.basketball.integration;

import com.rossotti.basketball.config.IntegrationConfig;
import com.rossotti.basketball.jpa.model.Game;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={IntegrationConfig.class})
@SpringBootTest
public class IntegrationFlowTest {
    @Autowired
    private GatewayService gatewayService;

    @Test
    public void testFlow_GameNotFound() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setGameDate("2016-10-27");
        serviceProperties.setGameTeam("chicago-zephyr's");
        List<Game> games = gatewayService.processGames(serviceProperties);
        Assert.assertTrue(games.size() == 0);
    }

    @Test
    public void testFlow_AsOfDateTeam_Completed() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setGameDate("2015-10-27");
        serviceProperties.setGameTeam("chicago-zephyr's");
        List<Game> games = gatewayService.processGames(serviceProperties);
        Assert.assertTrue(games.size() == 1);
    }

    @Test
    public void testFlow_AsOfDateTeam_Scheduled() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setGameDate("2016-10-27");
        serviceProperties.setGameTeam("st-louis-bomber's");
        List<Game> games = gatewayService.processGames(serviceProperties);
        Assert.assertTrue(games.size() == 1);
    }

    @Test
    public void testFlow_AsOfDate_Mixed_Multiple() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setGameDate("2016-10-28");
        List<Game> games = gatewayService.processGames(serviceProperties);
        Assert.assertTrue(games.size() == 2);
    }

      @Test
      public void testFlow_AsOfDateTeam_Roster_Single() {
          ServiceProperties serviceProperties = new ServiceProperties();
          serviceProperties.setGameDate("2016-10-29");
          serviceProperties.setGameTeam("st-louis-bomber's");
          List<Game> games = gatewayService.processGames(serviceProperties);
          Assert.assertTrue(games.size() == 1);
      }
}