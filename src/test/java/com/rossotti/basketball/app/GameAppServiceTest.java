package com.rossotti.basketball.app;

import com.rossotti.basketball.app.service.GameAppService;
import com.rossotti.basketball.jpa.model.BoxScore;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.Game.GameStatus;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.service.GameJpaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameAppServiceTest {
	@Mock
	private GameJpaService gameJpaService;

	@InjectMocks
	private GameAppService gameAppService;

	@Test
	public void findByAsOfDate_notFound() {
		when(gameJpaService.findByAsOfDate(anyObject()))
			.thenReturn(new ArrayList<>());
		List<Game> games = gameAppService.findByAsOfDate(LocalDate.of(1995, 11, 26));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByAsOfDate_found() {
		when(gameJpaService.findByAsOfDate(anyObject()))
			.thenReturn(createMockGames());
		List<Game> games = gameAppService.findByAsOfDate(LocalDate.of(1995, 11, 26));
		Assert.assertEquals(2, games.size());
	}

	@Test
	public void findByTeamKeyAsOfDate_notFound() {
		when(gameJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(null);
		Game game = gameAppService.findByTeamKeyAsOfDate("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertNull(game);
	}

	@Test
	public void findByTeamKeyAsOfDate_found() {
		when(gameJpaService.findByTeamKeyAndAsOfDate(anyString(), anyObject()))
			.thenReturn(createMockGame_Scheduled());
		Game game = gameAppService.findByTeamKeyAsOfDate("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertEquals(LocalDateTime.of(2015, 11, 26, 10, 0), game.getGameDateTime());
	}

	@Test
	public void findPreviousByTeamKeyAsOfDate_notFound() {
		when(gameJpaService.findPreviousByTeamKeyAsOfDate(anyString(), anyObject()))
			.thenReturn(null);
		LocalDateTime previousGameDate = gameAppService.findPreviousByTeamKeyAsOfDate("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertNull(previousGameDate);
	}

	@Test
	public void findPreviousByTeamKeyAsOfDate_found() {
		when(gameJpaService.findPreviousByTeamKeyAsOfDate(anyString(), anyObject()))
			.thenReturn(LocalDateTime.of(2015, 11, 26, 10, 0));
		LocalDateTime previousGameDate = gameAppService.findPreviousByTeamKeyAsOfDate("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertEquals(LocalDateTime.of(2015, 11, 26, 10, 0), previousGameDate);
	}

	@Test
	public void findByDateTeamSeason_notFound() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(new ArrayList<>());
		List<Game> games = gameAppService.findByTeamKeyAsOfDateSeason("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByDateTeamSeason_found() {
		when(gameJpaService.findByTeamKeyAndAsOfDateSeason(anyString(), anyObject()))
			.thenReturn(createMockGames());
		List<Game> games = gameAppService.findByTeamKeyAsOfDateSeason("sacramento-hornets", LocalDate.of(1995, 11, 26));
		Assert.assertEquals(2, games.size());
	}

	@Test
	public void updateGame_notFound() {
		when(gameJpaService.update(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.NotFound));
		Game game = gameAppService.updateGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void updateGame_updated() {
		when(gameJpaService.update(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Updated));
		Game game = gameAppService.updateGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isUpdated());
	}

	@Test
	public void createGame_found() {
		when(gameJpaService.create(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Found));
		Game game = gameAppService.createGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void createGame_created() {
		when(gameJpaService.create(anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Created));
		Game game = gameAppService.createGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isCreated());
	}

	private List<Game> createMockGames() {
		return Arrays.asList(
			createMockGame_Completed(),
			createMockGame_Scheduled()
		);
	}

	private Game createMockGame_Scheduled() {
		Game game = new Game();
		game.setGameDateTime(LocalDateTime.of(2015, 11, 26, 10, 0));
		game.setStatus(GameStatus.Scheduled);
		Team teamHome = new Team();
		teamHome.setTeamKey("brooklyn-nets");
		BoxScore boxScoreHome = new BoxScore();
		boxScoreHome.setLocation(BoxScore.Location.Home);
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		Team teamAway = new Team();
		teamAway.setTeamKey("detroit-pistons");
		BoxScore boxScoreAway = new BoxScore();
		boxScoreAway.setLocation(BoxScore.Location.Away);
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}

	private Game createMockGame_Completed() {
		Game game = new Game();
		game.setGameDateTime(LocalDateTime.of(2015, 11, 26, 10, 0));
		game.setStatus(GameStatus.Completed);
		return game;
	}

	private Game createMockGame_StatusCode(StatusCodeDAO status) {
		Game game = new Game();
		game.setGameDateTime(LocalDateTime.of(2015, 11, 26, 10, 0));
		game.setStatusCode(status);
		return game;
	}
}