package com.rossotti.basketball.client.service;

import com.rossotti.basketball.app.exception.FileException;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.util.function.StreamConverter;
import com.rossotti.basketball.util.service.FileService;
import com.rossotti.basketball.util.service.PropertyService;
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
import java.time.LocalDate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestStatsServiceTest {

	@Mock
	private PropertyService propertyService;

	@Mock
	private RestClientService restClientService;

	@Mock
	private FileService fileService;

	@InjectMocks
	private RestStatsService restStatsService;

	@Test
	public void retrieveBoxScore_PropertyException_PropertyService() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_PropertyException_ClientService() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_NotFound() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_Unauthorized() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_IOException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>("test".getBytes(), HttpStatus.OK));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_Found_PersistFalse() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(StreamConverter.getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json")), HttpStatus.OK));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", false);
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void retrieveBoxScore_PropertyException_PersistTrue() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(StreamConverter.getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json")), HttpStatus.OK));
		when(propertyService.getProperty_Path(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", true);
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_FileException_PersistTrue() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(StreamConverter.getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json")), HttpStatus.OK));
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("//");
		when(fileService.fileStreamWriter(anyString(), any(byte[].class)))
			.thenThrow(new FileException("IO Exception"));
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", true);
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_Found_PersistTrue() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.getJson(anyString()))
			.thenReturn(new ResponseEntity<>(StreamConverter.getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json")), HttpStatus.OK));
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("//");
		when(fileService.fileStreamWriter(anyString(), any(byte[].class)))
			.thenReturn(true);
		GameDTO game = restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics", true);
		Assert.assertTrue(game.isFound());
	}

//	@Test
//	public void retrieveRoster_PropertyException_PropertyService() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenThrow(new PropertyException("propertyName"));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isServerException());
//	}
//
//	@Test
//	public void retrieveRoster_PropertyException_ClientService() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.getJson(anyString()))
//			.thenThrow(new PropertyException("propertyName"));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isServerException());
//	}
//
//	@Test
//	public void retrieveRoster_NotFound() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.getJson(anyString()))
//			.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isNotFound());
//	}
//
//	@Test
//	public void retrieveRoster_Unauthorized() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.getJson(anyString()))
//			.thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isNotFound());
//	}
//
//	@Test
//	public void retrieveRoster_IOException() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.getJson(anyString()))
//			.thenReturn(new ResponseEntity<>("test".getBytes(), HttpStatus.OK));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isServerException());
//	}

//	@Test
//	public void retrieveRoster_Found() {
//		when(propertyService.getProperty_Http(anyString()))
//			.thenReturn("https://");
//		when(restClientService.getJson(anyString()))
//			.thenReturn(new ResponseEntity<>(getBytes(getClass().getClassLoader().getResourceAsStream("mockClient/rosterClient.json")), HttpStatus.OK));
//		RosterDTO roster = restStatsService.retrieveRoster("houston-rockets", LocalDate.of(2016, 3, 11));
//		Assert.assertTrue(roster.isFound());
//	}
}