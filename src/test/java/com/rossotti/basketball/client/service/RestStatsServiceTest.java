package com.rossotti.basketball.client.service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestStatsServiceTest {

	@Mock
	private PropertyService propertyService;

	@Mock
	private RestClientService restClientService;

	@InjectMocks
	private RestStatsService restStatsService;

	@Test
	public void retrieveBoxScore_PropertyException_PropertyService() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_PropertyException_ClientService() {
		when(propertyService.getProperty_Http(anyString()))
				.thenReturn("https://");
		when(restClientService.getJson(anyString()))
				.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_NotFound() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_Unauthorized() {
		when(propertyService.getProperty_Http(anyString()))
				.thenReturn("https://");
		when(restClientService.getJson(anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_IOException() {
		when(propertyService.getProperty_Http(anyString()))
				.thenReturn("https://");
		when(restClientService.getJson(anyString()))
				.thenReturn(new ResponseEntity<>("test".getBytes(), HttpStatus.OK));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_Found() {
		when(propertyService.getProperty_Http(anyString()))
				.thenReturn("https://");
		when(restClientService.getJson(anyString()))
				.thenReturn(new ResponseEntity<>(getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json")), HttpStatus.OK));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isFound());
	}

//	@Test
//	public void retrieveRoster_propertyException() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenThrow(new PropertyException("propertyName"));
//		RosterDTO roster = restStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
//		Assert.assertTrue(roster.isServerException());
//	}
//
//	@Test
//	public void retrieveRoster_notFound() {
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		RosterDTO roster = restStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
//		Assert.assertTrue(roster.isNotFound());
//	}
//
//	@Test
//	public void retrieveRoster_clientException() {
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		RosterDTO roster = restStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
//		Assert.assertTrue(roster.isClientException());
//	}
//
//	@Test
//	public void retrieveRoster_found() {
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		RosterDTO roster = restStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
//		Assert.assertTrue(roster.isFound());
//	}
//
//	@Test
//	public void retrieveStandings_propertyException() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenThrow(new PropertyException("propertyName"));
//		StandingsDTO standings = restStatsService.retrieveStandings("20141108");
//		Assert.assertTrue(standings.isServerException());
//	}
//
//	@Test
//	public void retrieveStandings_notFound() {
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		StandingsDTO standings = restStatsService.retrieveStandings("20141108");
//		Assert.assertTrue(standings.isNotFound());
//	}
//
//	@Test
//	public void retrieveStandings_clientException() {
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		StandingsDTO standings = restStatsService.retrieveStandings("20141108");
//		Assert.assertTrue(standings.isClientException());
//	}
//
//	@Test
//	public void retrieveStandings_found() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
//			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
//		StandingsDTO standings = restStatsService.retrieveStandings("20141108");
//		Assert.assertTrue(standings.isFound());
//	}

	private byte[] getBytes(InputStream inputStream) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		try {
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
		}
		catch (IOException ioe) {
			return null;
		}
		return buffer.toByteArray();
	}
}