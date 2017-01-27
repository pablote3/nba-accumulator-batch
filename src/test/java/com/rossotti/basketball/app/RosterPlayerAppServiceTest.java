package com.rossotti.basketball.app;

import com.rossotti.basketball.app.service.RosterPlayerAppService;
import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.jpa.exception.DuplicateEntityException;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.service.RosterPlayerJpaService;
import com.rossotti.basketball.jpa.service.TeamJpaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterPlayerAppServiceTest {
	@Mock
	private RosterPlayerJpaService rosterPlayerJpaService;

	@Mock
	private TeamJpaService teamJpaService;

	@InjectMocks
	private RosterPlayerAppService rosterPlayerAppService;

	@Test(expected=NoSuchEntityException.class)
	public void getBoxScorePlayers_notFound() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(anyString(), anyString(), anyString(), anyObject()))
			.thenReturn(createMockRosterPlayer("", "", StatusCodeDAO.NotFound));
		List<BoxScorePlayer> boxScorePlayers = rosterPlayerAppService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), createMockBoxScore(), LocalDate.of(1995, 11, 26), "sacramento-hornets");
		Assert.assertTrue(boxScorePlayers.size() == 0);
	}

	@Test
	public void getBoxScorePlayers_found() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(anyString(), anyString(), anyString(), anyObject()))
			.thenReturn(createMockRosterPlayer("Coors", "Adolph", StatusCodeDAO.Found));
		List<BoxScorePlayer> boxScorePlayers = rosterPlayerAppService.getBoxScorePlayers(createMockBoxScorePlayerDTOs(), createMockBoxScore(), LocalDate.of(1995, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, boxScorePlayers.size());
		Assert.assertEquals("Coors", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getLastName());
		Assert.assertEquals("Adolph", boxScorePlayers.get(1).getRosterPlayer().getPlayer().getFirstName());
	}
	
	@Test(expected=NoSuchEntityException.class)
	public void getRosterPlayers_notFound() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-mcnuggets", StatusCodeDAO.NotFound));
		List<RosterPlayer> rosterPlayers = rosterPlayerAppService.getRosterPlayers(createMockRosterPlayerDTOs(), LocalDate.of(1995, 11, 26), "sacramento-hornets");
		Assert.assertTrue(rosterPlayers.size() == 0);
	}

	@Test
	public void getRosterPlayers_found() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", StatusCodeDAO.Found));
		List<RosterPlayer> rosterPlayers = rosterPlayerAppService.getRosterPlayers(createMockRosterPlayerDTOs(), LocalDate.of(1995, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, rosterPlayers.size());
		Assert.assertEquals("Clayton", rosterPlayers.get(1).getPlayer().getLastName());
		Assert.assertEquals("Mark", rosterPlayers.get(1).getPlayer().getFirstName());
	}

	@Test
	public void findByPlayerNameTeamAsOfDate_notFound() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(anyString(), anyString(), anyString(), anyObject()))
			.thenReturn(createMockRosterPlayer("Simmons", "Richard", StatusCodeDAO.NotFound));
		RosterPlayer rosterPlayer = rosterPlayerAppService.findByPlayerNameTeamAsOfDate("Simmons", "Richard", "sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByPlayerNameTeamAsOfDate_found() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(anyString(), anyString(), anyString(), anyObject()))
			.thenReturn(createMockRosterPlayer("Simmons", "Gene", StatusCodeDAO.Found));
		RosterPlayer rosterPlayer = rosterPlayerAppService.findByPlayerNameTeamAsOfDate("Simmons", "Gene", "sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertEquals("Gene", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void findByPlayerNameBirthdateAsOfDate_notFound() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate(anyString(), anyString(), anyObject(), anyObject()))
			.thenReturn(createMockRosterPlayer("Simmons", "Richard", StatusCodeDAO.NotFound));
		RosterPlayer rosterPlayer = rosterPlayerAppService.findByPlayerNameBirthdateAsOfDate("Simmons", "Richard", LocalDate.of(1995, 11, 26), LocalDate.of(1995, 11, 26));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByPlayerNameBirthdateAsOfDate_found() {
		when(rosterPlayerJpaService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate(anyString(), anyString(), anyObject(), anyObject()))
			.thenReturn(createMockRosterPlayer("Simmons", "Gene", StatusCodeDAO.Found));
		RosterPlayer rosterPlayer = rosterPlayerAppService.findByPlayerNameBirthdateAsOfDate("Simmons", "Richard", LocalDate.of(1995, 11, 26), LocalDate.of(1995, 11, 26));
		Assert.assertEquals("Gene", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void findByTeamKeyAsOfDate_notFound() {
		when(rosterPlayerJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
				.thenReturn(new ArrayList<>());
		List<RosterPlayer> rosterPlayers = rosterPlayerAppService.findByTeamKeyAsOfDate(LocalDate.of(1995, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new ArrayList<RosterPlayer>(), rosterPlayers);
	}

	@Test
	public void findByTeamKeyAsOfDate_found() {
		when(rosterPlayerJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
				.thenReturn(createMockRosterPlayers());
		List<RosterPlayer> rosterPlayers = rosterPlayerAppService.findByTeamKeyAsOfDate(LocalDate.of(1995, 11, 26), "sacramento-hornets");		Assert.assertEquals(2, rosterPlayers.size());
		Assert.assertEquals("Simpson", rosterPlayers.get(1).getPlayer().getLastName());
		Assert.assertEquals("Lisa", rosterPlayers.get(1).getPlayer().getFirstName());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createRosterPlayer_alreadyExists() {
		when(rosterPlayerJpaService.create(anyObject()))
			.thenThrow(new DuplicateEntityException(RosterPlayer.class));
		RosterPlayer rosterPlayer = rosterPlayerAppService.createRosterPlayer(createMockRosterPlayer("Smith", "Emmitt", StatusCodeDAO.Found));
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void createRosterPlayer_created() {
		when(rosterPlayerJpaService.create(anyObject()))
			.thenReturn(createMockRosterPlayer("Payton", "Walter", StatusCodeDAO.Created));
		RosterPlayer rosterPlayer = rosterPlayerAppService.createRosterPlayer(createMockRosterPlayer("Payton", "Walter", StatusCodeDAO.Created));
		Assert.assertEquals("Walter", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isCreated());
	}

	@Test
	public void updateRosterPlayer_notFound() {
		when(rosterPlayerJpaService.update(anyObject()))
			.thenReturn(createMockRosterPlayer("Lima", "Roger", StatusCodeDAO.NotFound));
		RosterPlayer rosterPlayer = rosterPlayerAppService.updateRosterPlayer(createMockRosterPlayer("Roger", "Lima", StatusCodeDAO.NotFound));
		Assert.assertEquals("Roger", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void updateRosterPlayer_updated() {
		when(rosterPlayerJpaService.update(anyObject()))
			.thenReturn(createMockRosterPlayer("Schaub", "Buddy", StatusCodeDAO.Updated));
		RosterPlayer rosterPlayer = rosterPlayerAppService.updateRosterPlayer(createMockRosterPlayer("Schaub", "Buddy", StatusCodeDAO.Found));
		Assert.assertEquals("Buddy", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isUpdated());
	}

	private BoxScorePlayerDTO[] createMockBoxScorePlayerDTOs() {
		BoxScorePlayerDTO[] boxScorePlayers = new BoxScorePlayerDTO[2];
		boxScorePlayers[0] = createMockBoxScorePlayerDTO("Adams", "Samuel");
		boxScorePlayers[1] = createMockBoxScorePlayerDTO("Coors", "Adolph");
		return boxScorePlayers;
	}

	private BoxScorePlayerDTO createMockBoxScorePlayerDTO(String lastName, String firstName) {
		BoxScorePlayerDTO boxScorePlayer = new BoxScorePlayerDTO();
		boxScorePlayer.setLast_name(lastName);
		boxScorePlayer.setFirst_name(firstName);
		boxScorePlayer.setPosition("C");
		boxScorePlayer.setMinutes((short)25);
		boxScorePlayer.setIs_starter(true);
		boxScorePlayer.setPoints((short)12);
		boxScorePlayer.setAssists((short)3);
		boxScorePlayer.setTurnovers((short)0);
		boxScorePlayer.setSteals((short)2);
		boxScorePlayer.setBlocks((short)15);
		boxScorePlayer.setField_goals_attempted((short)8);
		boxScorePlayer.setField_goals_made((short)4);
		boxScorePlayer.setField_goal_percentage((float).5);
		boxScorePlayer.setThree_point_field_goals_attempted((short)3);
		boxScorePlayer.setThree_point_field_goals_made((short)1);
		boxScorePlayer.setThree_point_percentage((float).333);
		boxScorePlayer.setFree_throws_attempted((short)10);
		boxScorePlayer.setFree_throws_made((short)1);
		boxScorePlayer.setFree_throw_percentage((float).1);
		boxScorePlayer.setOffensive_rebounds((short)0);
		boxScorePlayer.setDefensive_rebounds((short)10);
		boxScorePlayer.setPersonal_fouls((short)4);
		return boxScorePlayer;
	}

	private RosterPlayerDTO[] createMockRosterPlayerDTOs() {
		RosterPlayerDTO[] rosterPlayers = new RosterPlayerDTO[2];
		rosterPlayers[0] = createMockRosterPlayerDTO("Marino", "Dan");
		rosterPlayers[1] = createMockRosterPlayerDTO("Clayton", "Mark");
		return rosterPlayers;
	}

	private RosterPlayerDTO createMockRosterPlayerDTO(String lastName, String firstName) {
		RosterPlayerDTO rosterPlayer = new RosterPlayerDTO();
		rosterPlayer.setLast_name(lastName);
		rosterPlayer.setFirst_name(firstName);
		rosterPlayer.setDisplay_name(firstName + " " + lastName);
		rosterPlayer.setHeight_in((short)82);
		rosterPlayer.setWeight_lb((short)200);
		rosterPlayer.setBirthdate(LocalDate.of(1995, 11, 26));
		rosterPlayer.setBirthplace("Kalamazoo, KS");
		rosterPlayer.setUniform_number("25");
		rosterPlayer.setPosition("G");
		return rosterPlayer;
	}

	private List<RosterPlayer> createMockRosterPlayers() {
		return Arrays.asList(
			createMockRosterPlayer("Simpson", "Homer", StatusCodeDAO.Found),
			createMockRosterPlayer("Simpson", "Lisa", StatusCodeDAO.Found)
		);
	}

	private RosterPlayer createMockRosterPlayer(String lastName, String firstName, StatusCodeDAO statusCode) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		Player player = new Player();
		rosterPlayer.setPlayer(player);
		rosterPlayer.setStatusCode(statusCode);
		rosterPlayer.setFromDate(LocalDate.of(2015, 11, 26));
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(LocalDate.of(1995, 11, 26));
		return rosterPlayer;
	}

	private Team createMockTeam(String teamKey, StatusCodeDAO statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setStatusCode(statusCode);
		return team;
	}

	private BoxScore createMockBoxScore() {
		return new BoxScore();
	}
}