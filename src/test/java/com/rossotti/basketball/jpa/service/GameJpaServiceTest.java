package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameJpaServiceTest {

	private GameJpaService gameJpaService;

	@Autowired
	public void setGameJpaService(GameJpaService gameJpaService) {
		this.gameJpaService = gameJpaService;
	}

	@Test
	public void getById() {
		Game game = gameJpaService.getById(5L);
		Assert.assertEquals(Game.GameStatus.Postponed, game.getStatus());
		Assert.assertEquals("Baltimore Bullets", game.getBoxScoreHome().getTeam().getFullName());
	}

	@Test
	public void listAll() {
		@SuppressWarnings("unchecked") List<Game> games = (List<Game>) gameJpaService.listAll();
		Assert.assertTrue(games.size() >= 9);
	}

	@Test
	public void findByTeamKeyAndAsOfDate_Found() {
		Game game = gameJpaService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 27));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), game.getGameDateTime());
		Assert.assertEquals("Harlem Globetrotter's", game.getBoxScoreAway().getTeam().getFullName());
		Assert.assertEquals(3, game.getGameOfficials().size());
		Assert.assertEquals("QuestionableCall", game.getGameOfficials().get(2).getOfficial().getLastName());
		Assert.assertTrue(game.getBoxScoreAway().getBoxScoreStats().getPoints().equals((short)98));
		Assert.assertEquals(1, game.getBoxScoreHome().getBoxScorePlayers().size());
		Assert.assertEquals(RosterPlayer.Position.SG, game.getBoxScoreHome().getBoxScorePlayers().get(0).getRosterPlayer().getPosition());
		Assert.assertEquals(2, game.getBoxScoreAway().getBoxScorePlayers().size());
		Assert.assertTrue(game.getBoxScoreAway().getBoxScorePlayers().get(1).getBoxScoreStats().getPoints().equals((short)5));
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_TeamKey() {
		Game game = gameJpaService.findByTeamKeyAndAsOfDate("chicago-zephyrd", LocalDate.of(2015, 10, 27));
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_AsOfDate() {
		Game game = gameJpaService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 20));
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDateSeason_Found() {
		List<Game> games = gameJpaService.findByTeamKeyAndAsOfDateSeason("chicago-zephyr's", LocalDate.of(2015, 10, 28));
		Assert.assertEquals(2, games.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), games.get(0).getGameDateTime());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 28, 20, 0), games.get(1).getGameDateTime());
	}

	@Test
	public void findByTeamKeyAndAsOfDateSeason_NotFound() {
		List<Game> games = gameJpaService.findByTeamKeyAndAsOfDateSeason("chicago-zephyr's", LocalDate.of(2015, 10, 25));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Game> games = gameJpaService.findByAsOfDate(LocalDate.of(2015, 10, 27));
		Assert.assertEquals(3, games.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 30), games.get(0).getGameDateTime());
		Assert.assertTrue(games.get(0).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 21, 0), games.get(1).getGameDateTime());
		Assert.assertTrue(games.get(1).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), games.get(2).getGameDateTime());
		Assert.assertTrue(games.get(2).isCompleted());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Game> games = gameJpaService.findByAsOfDate(LocalDate.of(2014, 10, 27));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findCountByAsOfDate_NotFound() {
		int count = gameJpaService.findCountByAsOfDate(LocalDate.of(2015, 10, 26));
		Assert.assertEquals(0, count);
	}

	@Test
	public void findCountByAsOfDate_Found() {
		int count = gameJpaService.findCountByAsOfDate(LocalDate.of(2015, 10, 27));
		Assert.assertEquals(3, count);
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_Found() {
		LocalDateTime gameDateTime = gameJpaService.findPreviousByTeamKeyAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 28, 20, 0), gameDateTime);
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_TeamKey() {
		LocalDateTime gameDateTime = gameJpaService.findPreviousByTeamKeyAsOfDate("chicago-zephyry", LocalDate.of(2015, 10, 30));
		Assert.assertNull(gameDateTime);
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_AsOfDate() {
		LocalDateTime gameDateTime = gameJpaService.findPreviousByTeamKeyAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 27));
		Assert.assertNull(gameDateTime);
	}

	@Test
	public void create_Created() {
		Game createGame = gameJpaService.create(createMockGame(LocalDateTime.of(2016, 10, 11, 22, 0), 1L, "chicago-zephyr's", 2L, "harlem-globetrotter's", Game.GameStatus.Scheduled));
		Game findGame = gameJpaService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2016, 10, 11));
		Assert.assertTrue(createGame.isCreated());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals("Harlem Globetrotter's", findGame.getBoxScoreAway().getTeam().getFullName());
	}

	@Test
	public void create_Exists() {
		Game createGame = gameJpaService.create(createMockGame(LocalDateTime.of(2015, 10, 27, 20, 30), 3L, "st-louis-bomber's", 4L, "salinas-cowboys", Game.GameStatus.Scheduled));
		Assert.assertTrue(createGame.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		gameJpaService.create(createMockGame(LocalDateTime.of(2016, 10, 13, 22, 0), 1L, "chicago-zephyr's", 2L, "harlem-globetrotter's", null));
	}

	@Test
	public void update_Updated() {
		Game updateGame = gameJpaService.update(updateMockGame(LocalDateTime.of(2015, 1, 7, 19, 0), 20L, "chicago-bulls", 21L, "utah-jazz", Game.GameStatus.Completed));
		Game findGame = gameJpaService.findByTeamKeyAndAsOfDate("chicago-bulls", LocalDate.of(2015, 1, 7));
		Assert.assertTrue(updateGame.isUpdated());
		Assert.assertEquals(Game.GameStatus.Completed, findGame.getStatus());
		Assert.assertEquals(3, findGame.getGameOfficials().size());
		Assert.assertEquals("MissedCa'll", findGame.getGameOfficials().get(1).getOfficial().getLastName());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals("Utah Jazz", findGame.getBoxScoreAway().getTeam().getFullName());
		Assert.assertTrue(findGame.getBoxScoreAway().getBoxScoreStats().getFreeThrowMade().equals((short)18));
		Assert.assertTrue(findGame.getBoxScoreHome().getBoxScoreStats().getFreeThrowMade().equals((short)10));
		Assert.assertEquals(1, findGame.getBoxScoreAway().getBoxScorePlayers().size());
		Assert.assertTrue(findGame.getBoxScoreAway().getBoxScorePlayers().get(0).getBoxScoreStats().getFreeThrowMade().equals((short)2));
		Assert.assertEquals(2, findGame.getBoxScoreHome().getBoxScorePlayers().size());
		Assert.assertTrue(findGame.getBoxScoreHome().getBoxScorePlayers().get(0).getBoxScoreStats().getFreeThrowMade().equals((short)4));
	}

	@Test
	public void update_NotFound_TeamKey() {
		Game updateGame = gameJpaService.update(updateMockGame(LocalDateTime.of(2015, 1, 7, 19, 0), 20L, "chicago-bulls", 21L, "utah-jazzers", Game.GameStatus.Completed));
		Assert.assertTrue(updateGame.isNotFound());
	}

	@Test
	public void update_NotFound_AsOfDateTime() {
		Game updateGame = gameJpaService.update(updateMockGame(LocalDateTime.of(2014, 1, 7, 19, 0), 20L, "chicago-bulls", 21L, "utah-jazz", Game.GameStatus.Completed));
		Assert.assertTrue(updateGame.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		gameJpaService.update(updateMockGame(LocalDateTime.of(2015, 1, 7, 19, 0), 20L, "chicago-bulls", 21L, "utah-jazz", null));
	}

	@Test
	public void delete_Deleted() {
		Game deleteGame = gameJpaService.delete(12L);
		Game findGame = gameJpaService.getById(12L);
		Assert.assertNull(findGame);
		Assert.assertTrue(deleteGame.isDeleted());
	}

	@Test
	public void delete_NotFound() {
		Game deleteGame = gameJpaService.delete(101L);
		Assert.assertTrue(deleteGame.isNotFound());
	}

	private Game createMockGame(LocalDateTime gameDateTime, Long teamIdHome, String teamKeyHome, Long teamIdAway, String teamKeyAway, Game.GameStatus status) {
		Game game = new Game();
		game.setGameDateTime(gameDateTime);
		game.setSeasonType(Game.SeasonType.Regular);
		game.setStatus(status);
		game.addBoxScore(createMockBoxScore(game, teamIdHome, teamKeyHome, BoxScore.Location.Home));
		game.addBoxScore(createMockBoxScore(game, teamIdAway, teamKeyAway, BoxScore.Location.Away));
		return game;
	}

	private BoxScore createMockBoxScore(Game game, Long teamId, String teamKey, BoxScore.Location location) {
		BoxScore boxScore = new BoxScore();
		boxScore.setGame(game);
		boxScore.setTeam(getMockTeam(teamId, teamKey));
		boxScore.setLocation(location);
		return boxScore;
	}

	private Team getMockTeam(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}

	private Game updateMockGame(LocalDateTime gameDateTime, Long teamIdHome, String teamKeyHome, Long teamIdAway, String teamKeyAway, Game.GameStatus status) {
		Game game = createMockGame(gameDateTime, teamIdHome, teamKeyHome, teamIdAway, teamKeyAway, status);
		game.addGameOfficial(createMockGameOfficial(game, 1L, "LateCall", "Joe"));
		game.addGameOfficial(createMockGameOfficial(game, 3L, "MissedCa'll", "Mike"));
		game.addGameOfficial(createMockGameOfficial(game, 4L, "QuestionableCall", "Hefe"));
		updateMockBoxScoreHome(game.getBoxScoreHome());
		updateMockBoxScoreAway(game.getBoxScoreAway());
		return game;
	}

	private GameOfficial createMockGameOfficial(Game game, Long officialId, String lastName, String firstName) {
		GameOfficial gameOfficial = new GameOfficial();
		gameOfficial.setGame(game);
		gameOfficial.setOfficial(getMockOfficial(officialId, lastName, firstName));
		return gameOfficial;
	}

	private Official getMockOfficial(Long officialId, String lastName, String firstName) {
		Official official = new Official();
		official.setId(officialId);
		official.setLastName(lastName);
		official.setFirstName(firstName);
		return official;
	}

	private void updateMockBoxScoreHome(BoxScore homeBoxScore) {
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_0());
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_1());
		homeBoxScore.setBoxScoreStats(new BoxScoreStats());
		homeBoxScore.getBoxScoreStats().setMinutes((short)240);
		homeBoxScore.getBoxScoreStats().setPoints((short)98);
		homeBoxScore.getBoxScoreStats().setAssists((short)14);
		homeBoxScore.getBoxScoreStats().setTurnovers((short)5);
		homeBoxScore.getBoxScoreStats().setSteals((short)7);
		homeBoxScore.getBoxScoreStats().setBlocks((short)5);
		homeBoxScore.getBoxScoreStats().setFieldGoalAttempts((short)44);
		homeBoxScore.getBoxScoreStats().setFieldGoalMade((short)22);
		homeBoxScore.getBoxScoreStats().setFieldGoalPercent((float).500);
		homeBoxScore.getBoxScoreStats().setThreePointAttempts((short)10);
		homeBoxScore.getBoxScoreStats().setThreePointMade((short)6);
		homeBoxScore.getBoxScoreStats().setThreePointPercent((float).6);
		homeBoxScore.getBoxScoreStats().setFreeThrowAttempts((short)20);
		homeBoxScore.getBoxScoreStats().setFreeThrowMade((short)10);
		homeBoxScore.getBoxScoreStats().setFreeThrowPercent((float).500);
		homeBoxScore.getBoxScoreStats().setReboundsOffense((short)25);
		homeBoxScore.getBoxScoreStats().setReboundsDefense((short)5);
		homeBoxScore.getBoxScoreStats().setPersonalFouls((short)18);
	}

	private void updateMockBoxScoreAway(BoxScore awayBoxScore) {
		awayBoxScore.addBoxScorePlayer(createMockBoxScorePlayerAway());
		awayBoxScore.setBoxScoreStats(new BoxScoreStats());
		awayBoxScore.getBoxScoreStats().setMinutes((short) 240);
		awayBoxScore.getBoxScoreStats().setPoints((short) 98);
		awayBoxScore.getBoxScoreStats().setAssists((short) 14);
		awayBoxScore.getBoxScoreStats().setTurnovers((short) 5);
		awayBoxScore.getBoxScoreStats().setSteals((short) 7);
		awayBoxScore.getBoxScoreStats().setBlocks((short) 5);
		awayBoxScore.getBoxScoreStats().setFieldGoalAttempts((short) 44);
		awayBoxScore.getBoxScoreStats().setFieldGoalMade((short) 22);
		awayBoxScore.getBoxScoreStats().setFieldGoalPercent((float) .500);
		awayBoxScore.getBoxScoreStats().setThreePointAttempts((short) 10);
		awayBoxScore.getBoxScoreStats().setThreePointMade((short) 6);
		awayBoxScore.getBoxScoreStats().setThreePointPercent((float) .6);
		awayBoxScore.getBoxScoreStats().setFreeThrowAttempts((short) 20);
		awayBoxScore.getBoxScoreStats().setFreeThrowMade((short) 18);
		awayBoxScore.getBoxScoreStats().setFreeThrowPercent((float) .500);
		awayBoxScore.getBoxScoreStats().setReboundsOffense((short) 25);
		awayBoxScore.getBoxScoreStats().setReboundsDefense((short) 5);
		awayBoxScore.getBoxScoreStats().setPersonalFouls((short) 18);
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_0() {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setBoxScoreStats(new BoxScoreStats());
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(2L, "Puzdrakiewicz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 11, 30), LocalDate.of(9999, 12, 31)));
		homeBoxScorePlayer.setPosition(RosterPlayer.Position.F);
		homeBoxScorePlayer.getBoxScoreStats().setFreeThrowMade((short)4);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_1() {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setBoxScoreStats(new BoxScoreStats());
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(3L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2009, 10, 30), LocalDate.of(2009, 11, 4)));
		homeBoxScorePlayer.setPosition(RosterPlayer.Position.C);
		homeBoxScorePlayer.getBoxScoreStats().setFreeThrowMade((short)0);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerAway() {
		BoxScorePlayer awayBoxScorePlayer = new BoxScorePlayer();
		awayBoxScorePlayer.setBoxScoreStats(new BoxScoreStats());
		awayBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(5L, "Puzdrakiewicz", "Junior", LocalDate.of(1966, 6, 10), LocalDate.of(2009, 10, 30), LocalDate.of(9999, 12, 31)));
		awayBoxScorePlayer.setPosition(RosterPlayer.Position.SG);
		awayBoxScorePlayer.getBoxScoreStats().setFreeThrowMade((short)2);
		return awayBoxScorePlayer;
	}

	private RosterPlayer getMockRosterPlayer(Long rosterPlayerId, String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setId(rosterPlayerId);
		rosterPlayer.setPlayer(getMockPlayer(lastName, firstName, birthdate));
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setPosition(RosterPlayer.Position.C);
		rosterPlayer.setNumber("99");
		return rosterPlayer;
	}

	private Player getMockPlayer(String lastName, String firstName, LocalDate birthdate) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		return player;
	}
}