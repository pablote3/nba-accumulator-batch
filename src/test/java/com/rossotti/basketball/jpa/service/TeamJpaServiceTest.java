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

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamJpaServiceTest {

	private TeamJpaService teamJpaService;

	@Autowired
	public void setTeamJpaService(TeamJpaService teamJpaService) {
		this.teamJpaService = teamJpaService;
	}

	@Test
	public void getById() {
		Team team = teamJpaService.getById(1L);
		Assert.assertEquals("Chicago Zephyr's", team.getFullName());
		Assert.assertTrue(team.getStandings().size() >= 1);
	}

	@Test
	public void listAll() {
		@SuppressWarnings("unchecked") List<Team> teams = (List<Team>) teamJpaService.listAll();
		Assert.assertTrue(teams.size() >= 11);
	}

	@Test
	public void findByKey_Found_FromDate() {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByKey_Found_ToDate() {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByKey_NotFound_TeamKey() {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("harlem-hooper's", LocalDate.of(2009, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByKey_NotFound_BeforeAsOfDate() {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByKey_NotFound_AfterAsOfDate() {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("harlem-globetrotter's", LocalDate.of(2016, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_Found_FromDate() {
		Team team = teamJpaService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByLastName_Found_ToDate() {
		Team team = teamJpaService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
		Assert.assertTrue(team.isFound());
	}

	@Test
	public void findByLastName_NotFound_TeamKey() {
		Team team = teamJpaService.findByLastNameAndAsOfDate("Globetreker's", LocalDate.of(2009, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_NotFound_BeforeAsOfDate() {
		Team team = teamJpaService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByLastName_NotFound_AfterAsOfDate() {
		Team team = teamJpaService.findByLastNameAndAsOfDate("Globetrotter's", LocalDate.of(2016, 7, 1));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findByTeamKey() {
		List<Team> teams = teamJpaService.findByTeamKey("salinas-cowboys");
		Assert.assertEquals("Salinas Cowboys", teams.get(0).getFullName());
	}

	@Test
	public void findByKey_Found() {
		List<Team> teams = teamJpaService.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(2, teams.size());
	}

	@Test
	public void findByKey_NotFound() {
		List<Team> teams = teamJpaService.findByTeamKey("st-louis-bombber's");
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void findByDateRange_Found() {
		List<Team> teams = teamJpaService.findByDate(LocalDate.of(2009, 10, 30));
		Assert.assertTrue(teams.size() >= 4);
	}

	@Test
	public void findByDateRange_NotFound() {
		List<Team> teams = teamJpaService.findByDate(LocalDate.of(1909, 10, 30));
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void create_Created_AsOfDate() {
		Team createTeam = teamJpaService.create(createMockTeam("sacramento-hornets", LocalDate.of(2012, 7, 1), LocalDate.of(9999, 12, 31), "Sacramento Hornets"));
		Team findTeam = teamJpaService.findByTeamKeyAndAsOfDate("sacramento-hornets", LocalDate.of(2012, 7, 1));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Sacramento Hornets", findTeam.getFullName());
	}

	@Test
	public void create_Created_DateRange() {
		Team createTeam = teamJpaService.create(createMockTeam("sacramento-rivercats", LocalDate.of(2006, 7, 1), LocalDate.of(2012, 7, 2), "Sacramento Rivercats"));
		Team findTeam = teamJpaService.findByTeamKeyAndAsOfDate("sacramento-rivercats", LocalDate.of(2006, 7, 1));
		Assert.assertTrue(createTeam.isCreated());
		Assert.assertEquals("Sacramento Rivercats", findTeam.getFullName());
	}

	@Test
	public void create_OverlappingDates() {
		Team createTeam = teamJpaService.create(createMockTeam("cleveland-rebels", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), "Cleveland Rebels"));
		Assert.assertTrue(createTeam.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		teamJpaService.create(createMockTeam("chavo-del-ocho", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), null));
	}

	@Test
	public void update_Updated() {
		Team updateTeam = teamJpaService.update(createMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(9999, 12, 31), "St. Louis Bombier's"));
		Team team = teamJpaService.findByTeamKeyAndAsOfDate("st-louis-bomber's", LocalDate.of(9999, 12, 31));
		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
		Assert.assertTrue(updateTeam.isUpdated());
	}

	@Test
	public void update_NotFound() {
		Team team = teamJpaService.update(createMockTeam("st-louis-bomb's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 7, 1), "St. Louis Bombier's"));
		Assert.assertTrue(team.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		teamJpaService.update(createMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
	}

	@Test
	public void delete_Deleted() {
		Team deleteTeam = teamJpaService.delete(7L);
		Team findTeam = teamJpaService.getById(7L);
		Assert.assertNull(findTeam);
		Assert.assertTrue(deleteTeam.isDeleted());
	}

	@Test
	public void delete_NotFound() {
		Team deleteTeam = teamJpaService.delete(101L);
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
