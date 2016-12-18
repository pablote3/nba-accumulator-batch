package com.rossotti.basketball.client.service;

//import com.rossotti.basketball.util.ThreadSleep;
import com.rossotti.basketball.client.dto.*;
import org.junit.Assert;
//import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestClientServiceTest {

	@Autowired
	private RestStatsService restStatsService;
//	private RestClientService restClientService;

//	@Before
//	public void setUp() {
//		ThreadSleep.sleep(0);
//	}

	@Ignore
	@Test
	public void retrieveRoster_200() {
		String event = "toronto-raptors";
		RosterDTO roster = restStatsService.retrieveRoster(event, LocalDate.of(2016, 12, 15));
		Assert.assertEquals(StatusCodeDTO.Found, roster.getStatusCode());
		Assert.assertEquals(15, roster.players.length);
	}

	@Ignore
	@Test
	public void retrieveStandings_200() {
		String event = "20141028";
		StandingsDTO standings = restStatsService.retrieveStandings(event);
		Assert.assertEquals(StatusCodeDTO.Found, standings.getStatusCode());
		Assert.assertEquals(30, standings.standing.length);
	}

	@Ignore
	@Test
	public void retrieveBoxScore_200() {
		String event = "20150415-utah-jazz-at-houston-rockets";
		GameDTO game = restStatsService.retrieveBoxScore(event);
		Assert.assertEquals(StatusCodeDTO.Found, game.getStatusCode());
		Assert.assertEquals(3, game.officials.length);
	}

//	@Test
//	public void retrieveRoster_401() throws IOException {
//		String accessToken = "badToken";
//		String userAgent = "validUserAgent";
//		client = buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
//		int status = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(401, status);
//	}
//
//	@Ignore
//	@Test
//	public void retrieveRoster_404() throws IOException {
//		String accessToken = "validAccessToken";
//		String userAgent = "validUserAgent";
//		client = buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raps.json";
//		int status = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(404, status);
//	}
//
//	@Ignore
//	@Test
//	public void retrieveRoster_403() throws IOException {
//		//could cause ban of IP
//		String accessToken = "validAccessToken";
//		String userAgent = "badUserAgent";
//		client = buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
//		int status = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(403, status);
//	}
//
//	@Ignore
//	@Test
//	public void retrieveRoster_429() throws IOException {
//		//sending more than 6 requests in a minute is counted against account
//		String accessToken = "validAccessToken";
//		String userAgent = "validUserAgent";
//		client = buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
//		int status200 = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(200, status200);
//		ThreadSleep.sleep(1);
//		int status429 = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(429, status429);
//	}
}