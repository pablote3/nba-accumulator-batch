package com.rossotti.basketball.integration;

import static org.junit.Assert.assertNotNull;

import com.rossotti.basketball.business.model.StandingsBusiness;
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

//    @Test
//    public void testFlow_GameNotFound() {
//        ServiceProperties serviceProperties = new ServiceProperties();
//        serviceProperties.setGameDate("2016-10-27");
//        serviceProperties.setGameTeam("chicago-zephyr's");
//        StandingsBusiness standingsBusiness = gatewayService.processGames(serviceProperties);
//        Assert.assertNull(standingsBusiness);
//    }

    @Test
    public void testFlow_AsOfDateTeam() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setGameDate("2015-10-27");
        serviceProperties.setGameTeam("chicago-zephyr's");
        List<Game> games = gatewayService.processGames(serviceProperties);
        System.out.println("done!");
    }
}