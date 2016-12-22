package com.rossotti.basketball.client.service;

import com.rossotti.basketball.util.service.exception.PropertyException;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.client.dto.StatsDTO.StatusCodeDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileStatsServiceTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileClientService fileClientService;

	@InjectMocks
	private FileStatsService fileStatsService;

	@Test
	public void retrieveBoxScore_PropertyException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_NotFound() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new GameDTO(), StatusCodeDTO.NotFound));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets");
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_ClientException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new GameDTO(), StatusCodeDTO.ClientException));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets");
		Assert.assertTrue(game.isClientException());
	}

	@Test
	public void retrieveBoxScore_Found() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new GameDTO(), StatusCodeDTO.Found));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets");
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void retrieveRoster_PropertyException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
		Assert.assertTrue(roster.isServerException());
	}

	@Test
	public void retrieveRoster_NotFound() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors-20150415", LocalDate.of(2015, 4, 15));
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveRoster_ClientException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
		Assert.assertTrue(roster.isClientException());
	}

	@Test
	public void retrieveRoster_Found() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new RosterDTO(), StatusCodeDTO.Found));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", LocalDate.of(2015, 4, 15));
		Assert.assertTrue(roster.isFound());
	}

	@Test
	public void retrieveStandings_PropertyException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028");
		Assert.assertTrue(standings.isServerException());
	}

	@Test
	public void retrieveStandings_NotFound() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new StandingsDTO(), StatusCodeDTO.NotFound));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028");
		Assert.assertTrue(standings.isNotFound());
	}

	@Test
	public void retrieveStandings_ClientException() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new StandingsDTO(), StatusCodeDTO.ClientException));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028");
		Assert.assertTrue(standings.isClientException());
	}

	@Test
	public void retrieveStandings_Found() {
		when(propertyService.getProperty_Path(anyString()))
			.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), anyObject()))
			.thenReturn(createMockStatsDTO(new StandingsDTO(), StatusCodeDTO.Found));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028");
		Assert.assertTrue(standings.isFound());
	}

	private StatsDTO createMockStatsDTO(StatsDTO stats, StatusCodeDTO statusCode) {
		stats.setStatusCode(statusCode);
		return stats;
	}
}