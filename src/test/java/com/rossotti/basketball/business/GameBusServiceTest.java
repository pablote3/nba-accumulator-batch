package com.rossotti.basketball.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.app.service.OfficialAppService;
import com.rossotti.basketball.app.service.RosterPlayerAppService;
import com.rossotti.basketball.app.service.TeamAppService;
import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.service.GameBusService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO.StatusCode;
import com.rossotti.basketball.client.service.FileStatsService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.BoxScore.Location;
import com.rossotti.basketball.jpa.model.Game.GameStatus;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import com.rossotti.basketball.util.service.exception.PropertyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("CanBeFinal")
@RunWith(MockitoJUnitRunner.class)
public class GameBusServiceTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileStatsService fileStatsService;

	@Mock
	private RestStatsService restStatsService;

	@Mock
	private RosterPlayerAppService rosterPlayerAppService;

	@Mock
	private OfficialAppService officialAppService;

	@Mock
	private TeamAppService teamAppService;

	@Mock
	private GameAppService gameAppService;

	@InjectMocks
	private GameBusService gameBusService;

	private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	@Test
	public void propertyService_propertyException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isServerError());
	}

	@Test
	public void propertyService_propertyNull() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(null);
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isServerError());
	}

	@Test
	public void fileClientService_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCode.NotFound));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isClientError());
	}

	@Test
	public void fileClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCode.ClientException));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isClientError());
	}

	@Test
	public void restClientService_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveBoxScore(anyString(), anyBoolean()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCode.NotFound));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isClientError());
	}

	@Test
	public void restClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveBoxScore(anyString(), anyBoolean()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCode.ClientException));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isClientError());
	}

	@Test
	public void rosterPlayerService_getBoxScorePlayers_appRosterUpdate() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerAppService.getBoxScorePlayers(anyObject(), anyObject(), anyString()))
			.thenThrow(new NoSuchEntityException(RosterPlayer.class));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isRosterUpdate());
	}

	@Test
	public void officialService_getGameOfficials_appOfficialError() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerAppService.getBoxScorePlayers(anyObject(), anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialAppService.getGameOfficials(anyObject(), anyObject()))
			.thenThrow(new NoSuchEntityException(Official.class));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isOfficialError());
	}

	@Test
	public void teamService_findTeam_appTeamError() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileStatsService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerAppService.getBoxScorePlayers(anyObject(), anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialAppService.getGameOfficials(anyObject(), anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamAppService.findTeamByTeamKey(anyString(), anyObject()))
			.thenThrow(new NoSuchEntityException(Team.class));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isTeamError());
	}

	@Test
	public void gameService_updateGame_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveBoxScore(anyString(), anyBoolean()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerAppService.getBoxScorePlayers(anyObject(), anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialAppService.getGameOfficials(anyObject(), anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamAppService.findTeamByTeamKey(anyString(), anyObject()))
			.thenReturn(createMockTeam_Found());
		when(gameAppService.updateGame(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.NotFound));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isServerError());
	}

	@Test
	public void gameService_updateGame_complete() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveBoxScore(anyString(), anyBoolean()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerAppService.getBoxScorePlayers(anyObject(), anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialAppService.getGameOfficials(anyObject(), anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamAppService.findTeamByTeamKey(anyString(), anyObject()))
			.thenReturn(createMockTeam_Found());
		when(gameAppService.updateGame(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Updated));
		GameBusiness game = gameBusService.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isCompleted());
	}

	private Game createMockGame_Scheduled() {
		Game game = new Game();
		game.setGameDateTime(LocalDateTime.of(2015, 11, 26, 10, 0));
		game.setStatus(GameStatus.Scheduled);
		Team teamHome = new Team();
		teamHome.setTeamKey("brooklyn-nets");
		BoxScore boxScoreHome = new BoxScore();
		boxScoreHome.setLocation(Location.Home);
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		Team teamAway = new Team();
		teamAway.setTeamKey("detroit-pistons");
		BoxScore boxScoreAway = new BoxScore();
		boxScoreAway.setLocation(Location.Away);
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}

	private Game createMockGame_StatusCode(StatusCodeDAO status) {
		Game game = new Game();
		game.setGameDateTime(LocalDateTime.of(2015, 11, 24, 10, 0));
		game.setStatusCode(status);
		return game;
	}

	private GameDTO createMockGameDTO_Found() {
		GameDTO game;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json");
			game = objectMapper.readValue(baseJson, GameDTO.class);
			game.setStatusCode(StatusCode.Found);
		}
		catch (IOException e) {
			game = new GameDTO();
			game.setStatusCode(StatusCode.ClientException);
		}
		return game;
	}

	private GameDTO createMockGameDTO_StatusCode(StatusCode statusCode) {
		GameDTO game = new GameDTO();
		game.setStatusCode(statusCode);
		return game;
	}

	private List<BoxScorePlayer> createMockBoxScorePlayers_Found() {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<>();
		boxScorePlayers.add(createMockBoxScorePlayer(1L, "Bogdanović", "Bojan"));
		boxScorePlayers.add(createMockBoxScorePlayer(2L, "Larkin", "DeShane"));
		boxScorePlayers.add(createMockBoxScorePlayer(3L, "Robinson", "Thomas"));
		boxScorePlayers.add(createMockBoxScorePlayer(4L, "Karasev", "Sergey"));
		return boxScorePlayers;
	}

	private BoxScorePlayer createMockBoxScorePlayer(Long id, String lastName, String firstName) {
		BoxScorePlayer boxScorePlayer = new BoxScorePlayer();
		boxScorePlayer.setId(id);
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(player);
		boxScorePlayer.setRosterPlayer(rosterPlayer);
		return boxScorePlayer;
	}

	private List<GameOfficial> createMockGameOfficials_Found() {
		List<GameOfficial> gameOfficials = new ArrayList<>();
		gameOfficials.add(createMockGameOfficial(1L, "Zarba", "Zach"));
		gameOfficials.add(createMockGameOfficial(2L, "Forte", "Brian"));
		gameOfficials.add(createMockGameOfficial(3L, "Roe", "Eli"));
		return gameOfficials;
	}

	private GameOfficial createMockGameOfficial(Long id, String lastName, String firstName) {
		GameOfficial gameOfficial = new GameOfficial();
		Official official = new Official();
		official.setId(id);
		official.setLastName(lastName);
		official.setFirstName(firstName);
		gameOfficial.setOfficial(official);
		return gameOfficial;
	}

	private Team createMockTeam_Found() {
		Team team = new Team();
		team.setId(1L);
		team.setTeamKey("brooklyn-nets");
		return team;
	}
}