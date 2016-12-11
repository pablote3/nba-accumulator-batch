package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.model.Team.Conference;
import com.rossotti.basketball.jpa.model.Team.Division;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {

	private TeamService teamService;

	@Autowired
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}

	@Test
	public void getById() {
		Team team = teamService.getById(1L);
		Assert.assertEquals("Chicago Zephyr's", team.getFullName());
		Assert.assertEquals(1, team.getStandings().size());
	}

	@Test
	public void listAll() {
		List<Team> teams = (List<Team>)teamService.listAll();
		Assert.assertTrue(teams.size() >= 11);
	}

	@Test
	public void findByKey_Found_FromDate() {
		Team team = teamService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByKey_Found_ToDate() {
		Team team = teamService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByKey_NotFound_TeamKey() {
		Team team = teamService.findByTeamKeyAndAsOfDate("harlem-hooper's", LocalDate.of(2009, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByKey_NotFound_BeforeAsOfDate() {
		Team team = teamService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByKey_NotFound_AfterAsOfDate() {
		Team team = teamService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2010, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_Found_FromDate() {
		Team team = teamService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByLastName_Found_ToDate() {
		Team team = teamService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByLastName_NotFound_TeamKey() {
		Team team = teamService.findByLastNameAndAsOfDate("Globetreker's", LocalDate.of(2009, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_NotFound_BeforeAsOfDate() {
		Team team = teamService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_NotFound_AfterAsOfDate() {
		Team team = teamService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2010, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByTeamKey() {
		List<Team> teams = teamService.findByTeamKey("salinas-cowboys");
		Assert.assertEquals("Salinas Cowboys", teams.get(0).getFullName());
	}

	@Test
	public void findByKey_Found() {
		List<Team> teams = teamService.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(2, teams.size());
	}

	@Test
	public void findByKey_NotFound() {
		List<Team> teams = teamService.findByTeamKey("st-louis-bombber's");
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void findByDateRange_Found() {
		List<Team> teams = teamService.findByDate(LocalDate.of(2009, 10, 30));
		Assert.assertTrue(teams.size() >= 4);
	}

	@Test
	public void findByDateRange_NotFound() {
		List<Team> teams = teamService.findByDate(LocalDate.of(1909, 10, 30));
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void create_Created_AsOfDate() {
		Team createTeam = teamService.create(createMockTeam("sacramento-hornets", LocalDate.of(2012, 7, 1), LocalDate.of(9999, 12, 31), "Sacramento Hornets"));
		Team findTeam = teamService.findByTeamKeyAndAsOfDate("sacramento-hornets", LocalDate.of(2012, 7, 1));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Sacramento Hornets", findTeam.getFullName());
	}

	@Test
	public void create_Created_DateRange() {
		Team createTeam = teamService.create(createMockTeam("sacramento-rivercats", LocalDate.of(2006, 7, 1), LocalDate.of(2012, 7, 2), "Sacramento Rivercats"));
		Team findTeam = teamService.findByTeamKeyAndAsOfDate("sacramento-rivercats", LocalDate.of(2006, 7, 1));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Sacramento Rivercats", findTeam.getFullName());
	}

	@Test
	public void create_OverlappingDates() {
		Team createTeam = teamService.create(createMockTeam("cleveland-rebels", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), "Cleveland Rebels"));
		Assert.assertTrue(createTeam.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		Team createTeam = teamService.create(createMockTeam("chavo-del-ocho", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), null));
	}

	@Test
	public void update_Updated() {
		Team updateTeam = teamService.update(createMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(9999, 12, 31), "St. Louis Bombier's"));
		Team team = teamService.findByTeamKeyAndAsOfDate("st-louis-bomber's", LocalDate.of(9999, 12, 31));
		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
		Assert.assertTrue(updateTeam.isUpdated());
	}

	@Test
	public void update_NotFound() {
		Team team = teamService.update(createMockTeam("st-louis-bomb's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 7, 1), "St. Louis Bombier's"));
		Assert.assertTrue(team.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		teamService.update(createMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
	}

	@Test
	public void delete_Deleted() {
		Team deleteTeam = teamService.delete(7L);
		Team findTeam = teamService.getById(7L);
		Assert.assertNull(findTeam);
		Assert.assertTrue(deleteTeam.isDeleted());
	}

	@Test
	public void delete_NotFound() {
		Team deleteTeam = teamService.delete(101L);
		Assert.assertTrue(deleteTeam.isNotFound());
	}

	private Team createMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
		Team team = new Team();
		team.setTeamKey(key);
		team.setFromDate(fromDate);
		team.setToDate(toDate);
		team.setAbbr("SEA");
		team.setFirstName("Seattle");
		team.setLastName("Supersonics");
		team.setConference(Conference.West);
		team.setDivision(Division.Pacific);
		team.setSiteName("Key Arena");
		team.setCity("Seattle");
		team.setState("WA");
		team.setFullName(fullName);
		return team;
	}
}
