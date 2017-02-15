package com.rossotti.basketball.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.model.StandingRecord;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.app.service.StandingAppService;
import com.rossotti.basketball.business.model.StandingsBusiness;
import com.rossotti.basketball.business.service.StandingBusService;
import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO.StatusCode;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import com.rossotti.basketball.util.service.exception.PropertyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandingsBusServiceTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileStatsService fileStatsService;

	@Mock
	private RestStatsService restStatsService;

	@Mock
	private StandingAppService standingAppService;

	@InjectMocks
	private StandingBusService standingsBusinessService;

	private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	@Test
	public void propertyService_propertyException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isServerError());
	}

	@Test
	public void fileClientService_standingsNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.NotFound));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void fileClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.ClientException));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void fileClientService_emptyList() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.Found));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void restClientService_standingsNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString(), anyBoolean()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.NotFound));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void restClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString(), anyBoolean()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.ClientException));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void restClientService_emptyList() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString(), anyBoolean()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.Found));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void standingsService_noSuchEntity_team() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCode.Found));
		when(standingAppService.getStandings(anyObject()))
			.thenThrow(new NoSuchEntityException(Team.class));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isClientError());
	}

	@Test
	public void standingsService_createStanding_exists() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createStandingsDTO_Found());
		when(standingAppService.getStandings(anyObject()))
			.thenReturn(createMockStandings());
		when(standingAppService.buildStandingsMap(anyObject(), anyObject()))
			.thenReturn(createMockStandingsMap());
		when(standingAppService.buildHeadToHeadMap(anyString(), anyObject(), anyObject()))
			.thenReturn(createMockHeadToHeadMap());
		when(standingAppService.calculateStrengthOfSchedule(anyString(), anyObject(), anyObject(), anyObject()))
			.thenReturn(new StandingRecord(5, 10, 20, 40));
		when(standingAppService.createStanding(anyObject()))
			.thenReturn(createMockStanding_StatusCode(StatusCodeDAO.Found));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isServerError());
	}

	@Test
	public void standingsService_createStanding_created() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveStandings(anyString()))
			.thenReturn(createStandingsDTO_Found());
		when(standingAppService.getStandings(anyObject()))
			.thenReturn(createMockStandings());
		when(standingAppService.buildStandingsMap(anyObject(), anyObject()))
			.thenReturn(createMockStandingsMap());
		when(standingAppService.buildHeadToHeadMap(anyString(), anyObject(), anyObject()))
			.thenReturn(createMockHeadToHeadMap());
		when(standingAppService.calculateStrengthOfSchedule(anyString(), anyObject(), anyObject(), anyObject()))
			.thenReturn(new StandingRecord(5, 10, 20, 40));
		when(standingAppService.createStanding(anyObject()))
			.thenReturn(createMockStanding_StatusCode(StatusCodeDAO.Created));
		StandingsBusiness standings = standingsBusinessService.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isCompleted());
	}

	private StandingsDTO createStandingsDTO_Found() {
		StandingsDTO standings;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/standingsClient.json");
			standings = objectMapper.readValue(baseJson, StandingsDTO.class);
			standings.setStatusCode(StatusCode.Found);
		}
		catch (IOException e) {
			standings = new StandingsDTO();
			standings.setStatusCode(StatusCode.ClientException);
		}
		return standings;
	}

	private StandingsDTO createMockStandingsDTO_StatusCode(StatusCode statusCode) {
		StandingsDTO standings = new StandingsDTO();
		standings.setStatusCode(statusCode);
		standings.standing = new StandingDTO[0];
		return standings;
	}

	private List<Standing> createMockStandings() {
		List<Standing> standings = new ArrayList<>();
		standings.add(createMockStanding());
		return standings;
	}

	private Standing createMockStanding() {
		Standing standing = new Standing();
		standing.setTeam(createMockTeam());
		standing.setStandingDate(new LocalDate(2015, 12, 27));
		return standing;
	}

	private Team createMockTeam() {
		Team team = new Team();
		team.setTeamKey("cleveland-cavaliers");
		return team;
	}

	private Map<String, StandingRecord> createMockStandingsMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<>();
		standingsMap.put("cleveland-cavaliers", new StandingRecord(5, 10, 0, 0));
		return standingsMap;
	}

	private Map<String, StandingRecord> createMockHeadToHeadMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<>();
		standingsMap.put("cleveland-cavaliers", new StandingRecord(5, 10, 0, 0));
		return standingsMap;
	}

	private Standing createMockStanding_StatusCode(StatusCodeDAO status) {
		Standing standing = new Standing();
		standing.setStatusCode(status);
		return standing;
	}
}