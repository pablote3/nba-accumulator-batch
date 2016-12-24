package com.rossotti.basketball.app;

import com.rossotti.basketball.app.model.StandingRecord;
import com.rossotti.basketball.app.service.StandingAppService;
import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.jpa.exception.DuplicateEntityException;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.service.GameJpaService;
import com.rossotti.basketball.jpa.service.StandingJpaService;
import com.rossotti.basketball.jpa.service.TeamJpaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandingAppServiceTest {
	@Mock
	private StandingJpaService standingJpaService;

	@Mock
	private GameJpaService gameJpaService;

	@Mock
	private TeamJpaService teamJpaService;

	@InjectMocks
	private StandingAppService standingAppService;

	@Test(expected=NoSuchEntityException.class)
	public void findByTeamKeyAndAsOfDate_teamNotFound() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCodeDAO.NotFound));
		List<Standing> standings = standingAppService.getStandings(createMockStandingsDTO_teamNotFound());
		Assert.assertTrue(standings.size() == 0);
	}

	@Test
	public void findByTeamKeyAndAsOfDate_teamFound() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCodeDAO.Found));
		List<Standing> standings = standingAppService.getStandings(createMockStandingsDTO_teamFound());
		Assert.assertEquals(1, standings.size());
		Assert.assertEquals("denver-nuggets", standings.get(0).getTeam().getTeamKey());
		Assert.assertTrue(standings.get(0).isFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_notFound() {
		when(standingJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockStanding("denver-mcnuggets", null, null, null, null, StatusCodeDAO.NotFound));
		Standing standing = standingAppService.findStanding("denver-mcnuggets", LocalDate.of(2015, 11, 26));
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_found() {
		when(standingJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockStanding("denver-nuggets", (short)4, (short)7, 18, 35, StatusCodeDAO.Found));
		Standing standing = standingAppService.findStanding("denver-nuggets", LocalDate.of(2015, 11, 26));
		Assert.assertEquals("denver-nuggets", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isFound());
	}

	@Test
	public void findByAsOfDate_notFound() {
		when(standingJpaService.findByAsOfDate(anyObject()))
			.thenReturn(new ArrayList<>());
		List<Standing> standings = standingAppService.findStandings(LocalDate.of(2015, 11, 26));
		Assert.assertEquals(new ArrayList<Standing>(), standings);
	}

	@Test
	public void findByAsOfDate_found() {
		when(standingJpaService.findByAsOfDate(anyObject()))
			.thenReturn(createMockStandings());
		List<Standing> standings = standingAppService.findStandings(LocalDate.of(2015, 11, 26));
		Assert.assertEquals(5, standings.size());
		Assert.assertEquals("utah-jazz", standings.get(1).getTeam().getTeamKey());
		Assert.assertTrue(standings.get(1).isFound());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createStanding_alreadyExists() {
		when(standingJpaService.create(anyObject()))
			.thenThrow(new DuplicateEntityException(Standing.class));
		Standing standing = standingAppService.createStanding(createMockStanding("houston-rockets", (short)7, (short)10, null, null, StatusCodeDAO.Found));
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void createStanding_created() {
		when(standingJpaService.create(anyObject()))
			.thenReturn(createMockStanding("sacramento-kings",(short)1, (short)4, null, null, StatusCodeDAO.Created));
		Standing standing = standingAppService.createStanding(createMockStanding("sacramento-kings", (short)5, (short)15, null, null, StatusCodeDAO.NotFound));
		Assert.assertEquals("sacramento-kings", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isCreated());
	}

	@Test
	public void updateStanding_notFound() {
		when(standingJpaService.update(anyObject()))
			.thenReturn(createMockStanding("seattle-supersonics", null, null, null, null, StatusCodeDAO.NotFound));
		Standing standing = standingAppService.updateStanding(createMockStanding("seattle-supersonics", null, null, null, null, StatusCodeDAO.NotFound));
		Assert.assertEquals("seattle-supersonics", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void updateStanding_updated() {
		when(standingJpaService.update(anyObject()))
			.thenReturn(createMockStanding("toronto-raptors", (short)10, (short)15, 25, 35, StatusCodeDAO.Updated));
		Standing standing = standingAppService.updateStanding(createMockStanding("toronto-raptors", (short)18, (short)22, 42, 68, StatusCodeDAO.Found));
		Assert.assertEquals("toronto-raptors", standing.getTeam().getTeamKey());
		Assert.assertTrue(standing.isUpdated());
	}

	@Test
	public void deleteStandings_notFound() {
		when(standingJpaService.findByAsOfDate(anyObject()))
			.thenReturn(new ArrayList<>());
		List<Standing> standings = standingAppService.deleteStandings(anyObject());
		Assert.assertTrue(standings.size() == 0);
	}

	@Test
	public void deleteStandings_deleted() {
		when(standingJpaService.findByAsOfDate(anyObject()))
			.thenReturn(createMockStandings());
		when(standingJpaService.delete(anyLong()))
			.thenReturn(createMockStanding("toronto-raptors", (short)4, (short)5, 18, 27, StatusCodeDAO.Deleted));
		List<Standing> standings = standingAppService.deleteStandings(LocalDate.of(2012, 6, 12));
		Assert.assertTrue(standings.get(0).isDeleted());
	}

	@Test
	public void buildStandingsMap_noEntries() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(new ArrayList<>());
		Map<String, StandingRecord> standingsMap = standingAppService.buildStandingsMap(new ArrayList<>(), LocalDate.of(2012, 6, 12));
		Assert.assertEquals(new HashMap<String, StandingRecord>(), standingsMap);
	}

	@Test
	public void buildStandingsMap_hasEntries() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(createMockGames_Kings())
			.thenReturn(createMockGames_Jazz());
		Map<String, StandingRecord> standingsMap = standingAppService.buildStandingsMap(createMockStandings(), LocalDate.of(2012, 6, 12));
		Assert.assertEquals(5, standingsMap.size());
		Assert.assertEquals(3, standingsMap.get("utah-jazz").getGamesWon().intValue());
		Assert.assertEquals(4, standingsMap.get("utah-jazz").getGamesPlayed().intValue());
		Assert.assertEquals(4, standingsMap.get("utah-jazz").getOpptGamesWon().intValue());
		Assert.assertEquals(11, standingsMap.get("utah-jazz").getOpptGamesPlayed().intValue());
	}

	@Test
	public void buildHeadToHeadMap() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(createMockGames_Kings())
			.thenReturn(createMockGames_Jazz());
		Map<String, StandingRecord> headToHeadMap;
		//headToHead map with entries
		headToHeadMap = standingAppService.buildHeadToHeadMap("sacramento-kings", LocalDate.of(2012, 6, 12), createMockStandingsMap());
		Assert.assertEquals(3, headToHeadMap.size());
		Assert.assertEquals(0, headToHeadMap.get("detroit-pistons").getGamesWon().intValue());
		Assert.assertEquals(1, headToHeadMap.get("detroit-pistons").getGamesPlayed().intValue());
		Assert.assertEquals(1, headToHeadMap.get("detroit-pistons").getOpptGamesWon().intValue());
		Assert.assertEquals(4, headToHeadMap.get("detroit-pistons").getOpptGamesPlayed().intValue());

		headToHeadMap = standingAppService.buildHeadToHeadMap("utah-jazz", LocalDate.of(2012, 6, 12), createMockStandingsMap());
		Assert.assertEquals(3, headToHeadMap.size());
		Assert.assertEquals(0, headToHeadMap.get("phoenix-suns").getGamesWon().intValue());
		Assert.assertEquals(2, headToHeadMap.get("phoenix-suns").getGamesPlayed().intValue());
		Assert.assertEquals(6, headToHeadMap.get("phoenix-suns").getOpptGamesWon().intValue());
		Assert.assertEquals(8, headToHeadMap.get("phoenix-suns").getOpptGamesPlayed().intValue());
	}

	@Test
	public void calculateStrengthOfSchedule() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(createMockGames_Kings())
			.thenReturn(createMockGames_Jazz());
		StandingRecord standingRecord = standingAppService.calculateStrengthOfSchedule("sacramento-kings", LocalDate.of(2012, 6, 12), createMockStandingsMap(), createMockHeadToHeadMap_Kings());
		Assert.assertEquals(3, standingRecord.getGamesWon().intValue());
		Assert.assertEquals(7, standingRecord.getGamesPlayed().intValue());
		Assert.assertEquals(-12, standingRecord.getOpptGamesWon().intValue());
		Assert.assertEquals(-19, standingRecord.getOpptGamesPlayed().intValue());
	}

	private StandingsDTO createMockStandingsDTO_teamNotFound() {
		StandingsDTO standings = new StandingsDTO();
		standings.standings_date = ZonedDateTime.parse("2015-11-29T18:00:00-05:00");
		StandingDTO[] standing = new StandingDTO[1];
		standing[0] = createMockStandingDTO("denver-mcnuggets");
		standings.standing = standing;
		return standings;
	}

	private StandingsDTO createMockStandingsDTO_teamFound() {
		StandingsDTO standings = new StandingsDTO();
		standings.standings_date = ZonedDateTime.parse("2015-11-29T18:00:00-05:00");
		StandingDTO[] standing = new StandingDTO[1];
		standing[0] = createMockStandingDTO("denver-nuggets");
		standings.standing = standing;
		return standings;
	}

	private StandingDTO createMockStandingDTO(String teamKey) {
		StandingDTO standing = new StandingDTO();
		standing.setTeam_id(teamKey);
		standing.setRank((short)1);
		standing.setOrdinal_rank("1st");
		standing.setWon((short)70);
		standing.setLost((short)12);
		standing.setStreak("W3");
		standing.setStreak_type("win");
		standing.setStreak_total((short)3);
		standing.setGames_back((float)0);
		standing.setPoints_for((short)5350);
		standing.setPoints_against((short)5041);
		standing.setHome_won((short)40);
		standing.setHome_lost((short)2);
		standing.setAway_won((short)30);
		standing.setAway_lost((short)10);
		standing.setConference_won((short)42);
		standing.setConference_lost((short)3);
		standing.setLast_five("4-1");
		standing.setLast_ten("9-1");
		standing.setGames_played((short)82);
		standing.setPoints_scored_per_game((float)103.4);
		standing.setPoints_allowed_per_game((float)99.2);
		standing.setWin_percentage((float).8537);
		standing.setPoint_differential((short)359);
		standing.setPoint_differential_per_game((float)3.8);
		return standing;
	}

	private List<Standing> createMockStandings() {
		return Arrays.asList(
			createMockStanding("sacramento-kings", (short)1, (short)4, 7, 11, StatusCodeDAO.Found),
			createMockStanding("utah-jazz", (short)3, (short)4, 3, 10, StatusCodeDAO.Found),
			createMockStanding("detroit-pistons", (short)1, (short)3, 3, 4, StatusCodeDAO.Found),
			createMockStanding("phoenix-suns", (short)1, (short)2, 3, 4, StatusCodeDAO.Found),
			createMockStanding("miami-heat", (short)2, (short)3, 3, 6, StatusCodeDAO.Found)
		);
	}

	private Standing createMockStanding(String teamKey, Short gamesWon, Short gamesPlayed, Integer opptGamesWon, Integer opptGamesPlayed, StatusCodeDAO statusCode) {
		Standing standing = new Standing();
		Team team = new Team();
		team.setTeamKey(teamKey);
		standing.setTeam(team);
		standing.setGamesWon(gamesWon);
		standing.setGamesPlayed(gamesPlayed);
		standing.setOpptGamesWon(opptGamesWon);
		standing.setOpptGamesPlayed(opptGamesPlayed);
		standing.setStatusCode(statusCode);
		return standing;
	}

	private Map<String, StandingRecord> createMockStandingsMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<>();
		standingsMap.put("sacramento-kings", new StandingRecord(1, 4, 0, 0));
		standingsMap.put("detroit-pistons", new StandingRecord(1, 3, 0, 0));
		standingsMap.put("miami-heat", new StandingRecord(2, 3, 0, 0));
		standingsMap.put("utah-jazz", new StandingRecord(3, 4, 0, 0));
		standingsMap.put("phoenix-suns", new StandingRecord(1, 2, 0, 0));
		return standingsMap;
	}

	private Map<String, StandingRecord> createMockHeadToHeadMap_Kings() {
		Map<String, StandingRecord> headToHeadMap = new HashMap<>();
		headToHeadMap.put("detroit-pistons", new StandingRecord(0, 1, 1, 3));
		headToHeadMap.put("miami-heat", new StandingRecord(2, 2, 4, 6));
		headToHeadMap.put("utah-jazz", new StandingRecord(1, 1, 3, 4));
		return headToHeadMap;
	}

	private Team createMockTeam(String teamKey, StatusCodeDAO statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setStatusCode(statusCode);
		return team;
	}

	private List<Game> createMockGames_Kings() {
		return Arrays.asList(
				createMockGame(LocalDateTime.of(2015, 12, 2, 10, 0), "detroit-pistons", "sacramento-kings"),
				createMockGame(LocalDateTime.of(2015, 12, 3, 10, 0), "sacramento-kings", "miami-heat"),
				createMockGame(LocalDateTime.of(2015, 12, 4, 10, 0), "miami-heat", "sacramento-kings"),
				createMockGame(LocalDateTime.of(2015, 12, 5, 10, 0), "utah-jazz", "sacramento-kings")
		);
	}

	private List<Game> createMockGames_Jazz() {
		return Arrays.asList(
				createMockGame(LocalDateTime.of(2015, 12, 2, 10, 0), "phoenix-suns", "utah-jazz"),
				createMockGame(LocalDateTime.of(2015, 12, 3, 10, 0), "detroit-pistons", "utah-jazz"),
				createMockGame(LocalDateTime.of(2015, 12, 4, 10, 0), "utah-jazz", "phoenix-suns"),
				createMockGame(LocalDateTime.of(2015, 12, 5, 10, 0), "utah-jazz", "sacramento-kings")
		);
	}

	private Game createMockGame(LocalDateTime gameDateTime, String homeTeamKey, String awayTeamKey) {
		Game game = new Game();
		game.setGameDateTime(gameDateTime);
		BoxScore boxScoreHome = new BoxScore();
		Team teamHome = new Team();
		teamHome.setTeamKey(homeTeamKey);
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		BoxScore boxScoreAway = new BoxScore();
		Team teamAway = new Team();
		teamAway.setTeamKey(awayTeamKey);
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}
}