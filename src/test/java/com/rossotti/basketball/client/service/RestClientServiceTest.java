package com.rossotti.basketball.client.service;

//import com.rossotti.basketball.util.ThreadSleep;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
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

//	@Ignore
	@Test
	public void retrieveRoster_200() throws IOException {
//		String baseUrl = "https://erikberg.com/nba/roster/";
		String event = "toronto-raptors";
		StatsDTO stats = restStatsService.retrieveRoster(event, LocalDate.of(2016, 12, 15));
		Assert.assertEquals(RosterDTO.class, stats.getClass());
		Assert.assertEquals(StatusCodeDTO.Found, stats.getStatusCode());
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