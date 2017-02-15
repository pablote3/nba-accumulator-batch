package com.rossotti.basketball.batch;

import com.rossotti.basketball.batch.exception.SkipStepException;
import com.rossotti.basketball.business.model.GameBusiness;
import com.rossotti.basketball.business.model.RosterPlayerBusiness;
import com.rossotti.basketball.business.model.StatusCodeBusiness.StatusCode;
import com.rossotti.basketball.business.service.GameBusService;
import com.rossotti.basketball.business.service.RosterPlayerBusService;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.BoxScore.Location;
import com.rossotti.basketball.jpa.model.Game.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.joda.time.LocalDateTime;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

//@SuppressWarnings("CanBeFinal")
@RunWith(MockitoJUnitRunner.class)
public class GameProcessorTest {
	@Mock
	private GameBusService gameBusService;

	@Mock
	private RosterPlayerBusService rosterPlayerBusService;

	@InjectMocks
	private GameProcessor gameProcessor;

	@Test(expected=SkipStepException.class)
	public void gameBusService_scoreGame_clientError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.ClientError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test(expected=SkipStepException.class)
	public void gameBusService_scoreGame_serverError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.ServerError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test(expected=SkipStepException.class)
	public void gameBusService_scoreGame_officialError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.OfficialError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test(expected=SkipStepException.class)
	public void gameBusService_scoreGame_teamError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.TeamError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test(expected=SkipStepException.class)
	public void gameBusService_loadRoster_clientError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.RosterUpdate));
		when(rosterPlayerBusService.loadRoster(anyString(), anyString()))
			.thenReturn(createMockRosterPlayerBusiness(StatusCode.ClientError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test(expected=SkipStepException.class)
	public void gameBusService_loadRoster_serverError() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.RosterUpdate));
		when(rosterPlayerBusService.loadRoster(anyString(), anyString()))
			.thenReturn(createMockRosterPlayerBusiness(StatusCode.ServerError));
		gameProcessor.process(createMockGame_Scheduled());
	}

	@Test
	public void gameBusService_loadRoster1_completed() throws Exception {
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.RosterUpdate));
		when(rosterPlayerBusService.loadRoster(anyString(), anyString()))
			.thenReturn(createMockRosterPlayerBusiness(StatusCode.Completed));
		when(gameBusService.scoreGame(anyObject()))
			.thenReturn(createMockGameBusiness(StatusCode.Completed));
		gameProcessor.process(createMockGame_Scheduled());
	}

	private GameBusiness createMockGameBusiness(StatusCode statusCode) {
		GameBusiness gameBusiness = new GameBusiness(createMockGame_Scheduled());
		gameBusiness.setStatusCode(statusCode);
		return gameBusiness;
	}

	private RosterPlayerBusiness createMockRosterPlayerBusiness(StatusCode statusCode) {
		RosterPlayerBusiness rosterPlayerBusiness = new RosterPlayerBusiness();
		rosterPlayerBusiness.setStatusCode(statusCode);
		return rosterPlayerBusiness;
	}

	private Game createMockGame_Scheduled() {
		Game game = new Game();
		game.setGameDateTime(new LocalDateTime(2015, 11, 26, 10, 0));
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
}