package com.rossotti.basketball.app;

import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.service.TeamJpaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamAppServiceTest {
	@Mock
	private TeamJpaService teamJpaService;

	@InjectMocks
	private TeamAppService teamAppService;

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByTeamKey_notFound() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("new-orleans-hornets", "Hornets", StatusCodeDAO.NotFound));
		Team team = teamAppService.findTeamByTeamKey("new-orleans-hornets", LocalDate.of(2015, 11, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findTeamByTeamKey_found() {
		when(teamJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", "Nuggets", StatusCodeDAO.Found));
		Team team = teamAppService.findTeamByTeamKey("denver-nuggets", LocalDate.of(2015, 11, 26));
		Assert.assertEquals("denver-nuggets", team.getTeamKey());
		Assert.assertTrue(team.isFound());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findTeamByLastName_notFound() {
		when(teamJpaService.findByLastNameAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("new-orleans-hornets", "Hornets", StatusCodeDAO.NotFound));
		Team team = teamAppService.findTeamByLastName("Hornets", LocalDate.of(2015, 11, 26));
		Assert.assertTrue(team.isNotFound());
	}

	@Test
	public void findTeamByLastName_found() {
		when(teamJpaService.findByLastNameAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockTeam("denver-nuggets", "Nuggets", StatusCodeDAO.Found));
		Team team = teamAppService.findTeamByLastName("Hornets", LocalDate.of(2015, 11, 26));
		Assert.assertEquals("Nuggets", team.getLastName());
		Assert.assertTrue(team.isFound());
	}

	private Team createMockTeam(String teamKey, String lastName, StatusCodeDAO statusCode) {
		Team team = new Team();
		team.setTeamKey(teamKey);
		team.setLastName(lastName);
		team.setFromDate(LocalDate.of(2015, 11, 26));
		team.setToDate(LocalDate.of(2016, 11, 26));
		team.setStatusCode(statusCode);
		return team;
	}
}