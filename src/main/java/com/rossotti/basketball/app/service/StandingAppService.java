package com.rossotti.basketball.app.service;

import com.rossotti.basketball.app.model.StandingRecord;
import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.service.*;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StandingAppService {
	private final StandingJpaService standingJpaService;

	private final TeamJpaService teamJpaService;

	private final GameJpaService gameJpaService;

	private final Logger logger = LoggerFactory.getLogger(StandingAppService.class);

	@Autowired
	public StandingAppService(StandingJpaService standingJpaService, GameJpaService gameJpaService, TeamJpaService teamJpaService) {
		this.standingJpaService = standingJpaService;
		this.gameJpaService = gameJpaService;
		this.teamJpaService = teamJpaService;
	}

	public List<Standing> getStandings(StandingsDTO standingsDTO) {
		LocalDate asOfDate = DateTimeConverter.getLocalDate(standingsDTO.standings_date);
		List<Standing> standings = new ArrayList<>();
		if (standingsDTO.standing != null) {
			for (int i = 0; i < standingsDTO.standing.length; i++) {
				StandingDTO standingDTO = standingsDTO.standing[i];
				Team team = teamJpaService.findByTeamKeyAndAsOfDate(standingDTO.getTeam_id(), asOfDate);
				if (team.isNotFound()) {
					logger.info("Team not found " + standingDTO.getTeam_id());
					throw new NoSuchEntityException(Team.class);
				}
				Standing standing = new Standing();
				standing.setTeam(team);
				standing.setStandingDate(asOfDate);
				standing.setRank(standingDTO.getRank());
				standing.setOrdinalRank(standingDTO.getOrdinal_rank());
				standing.setGamesWon(standingDTO.getWon());
				standing.setGamesLost(standingDTO.getLost());
				standing.setStreak(standingDTO.getStreak());
				standing.setStreakType(standingDTO.getStreak_type());
				standing.setStreakTotal(standingDTO.getStreak_total());
				standing.setGamesBack(standingDTO.getGames_back());
				standing.setPointsFor(standingDTO.getPoints_for());
				standing.setPointsAgainst(standingDTO.getPoints_against());
				standing.setHomeWins(standingDTO.getHome_won());
				standing.setHomeLosses(standingDTO.getHome_lost());
				standing.setAwayWins(standingDTO.getAway_won());
				standing.setAwayLosses(standingDTO.getAway_lost());
				standing.setConferenceWins(standingDTO.getConference_won());
				standing.setConferenceLosses(standingDTO.getConference_lost());
				standing.setLastFive(standingDTO.getLast_five());
				standing.setLastTen(standingDTO.getLast_ten());
				standing.setGamesPlayed(standingDTO.getGames_played());
				standing.setPointsScoredPerGame(standingDTO.getPoints_scored_per_game());
				standing.setPointsAllowedPerGame(standingDTO.getPoints_allowed_per_game());
				standing.setWinPercentage(standingDTO.getWin_percentage());
				standing.setPointDifferential(standingDTO.getPoint_differential());
				standing.setPointDifferentialPerGame(standingDTO.getPoint_differential_per_game());
				standings.add(standing);
			}
		}
		return standings;
	}

	public Standing findStanding(String teamKey, LocalDate asOfDate) {
		return standingJpaService.findByTeamKeyAndAsOfDate(teamKey, asOfDate);
	}

	public List<Standing> findStandings(LocalDate asOfDate) {
		return standingJpaService.findByAsOfDate(asOfDate);
	}

	public Standing createStanding(Standing standing) {
		return standingJpaService.create(standing);
	}

	public Standing updateStanding(Standing standing) {
		return standingJpaService.update(standing);
	}

	public List<Standing> deleteStandings(LocalDate asOfDate) {
		List<Standing> standings = standingJpaService.findByAsOfDate(asOfDate);
		if (!standings.isEmpty() && standings.size() > 0) {
			logger.info("Deleting standings for " + DateTimeConverter.getStringDate(asOfDate));
			for (int i = 0; i < standings.size(); i++) {
				standings.set(i, standingJpaService.delete(standings.get(i).getId()));
			}
		}
		return standings;
	}

	public Map<String, StandingRecord> buildStandingsMap(List<Standing> standings, LocalDate asOfDate) {
		Map<String, StandingRecord> standingsMap = new HashMap<>();
		//create map with team games won/played
		for (Standing standing1 : standings) {
			StandingRecord standingRecord = new StandingRecord((int) standing1.getGamesWon(), (int) standing1.getGamesPlayed(), 0, 0);
			standingsMap.put(standing1.getTeam().getTeamKey(), standingRecord);
		}

		//update map summing opponent games won/played
		for (Standing standing : standings) {
			String teamKey = standing.getTeam().getTeamKey();
			Integer opptGamesWon = 0;
			Integer opptGamesPlayed = 0;
			List<Game> completeGames = gameJpaService.findByTeamKeyAndAsOfDateSeason(teamKey, asOfDate);
			for (Game completedGame : completeGames) {
				int opptBoxScoreId = completedGame.getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
				String opptTeamKey = completedGame.getBoxScores().get(opptBoxScoreId).getTeam().getTeamKey();
				opptGamesWon = opptGamesWon + standingsMap.get(opptTeamKey).getGamesWon();
				opptGamesPlayed = opptGamesPlayed + standingsMap.get(opptTeamKey).getGamesPlayed();

				String completedGameDate = DateTimeConverter.getStringDate(completedGame.getGameDateTime());
				logger.debug('\n' + ("  StandingsMap " + teamKey + " " + completedGameDate + " " + opptTeamKey +
						" Games Won/Played: " + standingsMap.get(opptTeamKey).getGamesWon() + " - " + standingsMap.get(opptTeamKey).getGamesPlayed()));
			}
			standingsMap.get(teamKey).setOpptGamesWon(opptGamesWon);
			standingsMap.get(teamKey).setOpptGamesPlayed(opptGamesPlayed);
		}
		return standingsMap;
	}

	public Map<String, StandingRecord> buildHeadToHeadMap(String teamKey, LocalDate asOfDate, Map<String, StandingRecord> standingsMap) {
		Map<String, StandingRecord> headToHeadMap = new HashMap<>();
		List<Game> completeGames = gameJpaService.findByTeamKeyAndAsOfDateSeason(teamKey, asOfDate);

		for (Game completeGame : completeGames) {
			int opptBoxScoreId = completeGame.getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
			BoxScore opptBoxScore = completeGame.getBoxScores().get(opptBoxScoreId);
			String opptTeamKey = opptBoxScore.getTeam().getTeamKey();
			Integer opptHeadToHeadResult = opptBoxScore.getResult() != null && opptBoxScore.getResult().equals(BoxScore.Result.Win) ? 1 : 0;
			if (headToHeadMap.get(opptTeamKey) == null) {
				headToHeadMap.put(opptTeamKey, new StandingRecord(opptHeadToHeadResult, 1, standingsMap.get(teamKey).getGamesWon(), standingsMap.get(teamKey).getGamesPlayed()));
			}
			else {
				headToHeadMap.get(opptTeamKey).setGamesWon(headToHeadMap.get(opptTeamKey).getGamesWon() + opptHeadToHeadResult);
				headToHeadMap.get(opptTeamKey).setGamesPlayed(headToHeadMap.get(opptTeamKey).getGamesPlayed() + 1);
				headToHeadMap.get(opptTeamKey).setOpptGamesWon(headToHeadMap.get(opptTeamKey).getOpptGamesWon() + standingsMap.get(teamKey).getGamesWon());
				headToHeadMap.get(opptTeamKey).setOpptGamesPlayed(headToHeadMap.get(opptTeamKey).getOpptGamesPlayed() + standingsMap.get(teamKey).getGamesPlayed());
			}
		}
		return headToHeadMap;
	}

	public StandingRecord calculateStrengthOfSchedule(String teamKey, LocalDate asOfDate, Map<String, StandingRecord> standingsMap, Map<String, StandingRecord> headToHeadMap) {
		BoxScore opptBoxScore;
		Integer opptGamesWon = 0;
		Integer opptGamesPlayed = 0;
		Integer opptOpptGamesWon = 0;
		Integer opptOpptGamesPlayed = 0;
		List<Game> completeGames = gameJpaService.findByTeamKeyAndAsOfDateSeason(teamKey, asOfDate);

		for (Game completeGame : completeGames) {
			int opptBoxScoreId = completeGame.getBoxScores().get(0).getTeam().getTeamKey().equals(teamKey) ? 1 : 0;
			opptBoxScore = completeGame.getBoxScores().get(opptBoxScoreId);
			String opptTeamKey = opptBoxScore.getTeam().getTeamKey();

			opptGamesWon = opptGamesWon + standingsMap.get(opptTeamKey).getGamesWon() - headToHeadMap.get(opptTeamKey).getGamesWon();
			opptGamesPlayed = opptGamesPlayed + standingsMap.get(opptTeamKey).getGamesPlayed() - headToHeadMap.get(opptTeamKey).getGamesPlayed();
			opptOpptGamesWon = opptOpptGamesWon + standingsMap.get(opptTeamKey).getOpptGamesWon() - headToHeadMap.get(opptTeamKey).getOpptGamesWon();
			opptOpptGamesPlayed = opptOpptGamesPlayed + standingsMap.get(opptTeamKey).getOpptGamesPlayed() - headToHeadMap.get(opptTeamKey).getOpptGamesPlayed();

			logger.debug('\n' + "SubTeamStanding " + opptTeamKey);
			logger.debug('\n' + "  Opponent Games Won/Played: " + opptGamesWon + " - " + opptGamesPlayed + " = " +
				standingsMap.get(opptTeamKey).getGamesWon() + " - " + standingsMap.get(opptTeamKey).getGamesPlayed() + " minus " +
				headToHeadMap.get(opptTeamKey).getGamesWon() + " - " + headToHeadMap.get(opptTeamKey).getGamesPlayed());
			logger.debug('\n' + "  OpptOppt Games Won/Played: " + opptOpptGamesWon + " - " + opptOpptGamesPlayed + " = " +
				standingsMap.get(opptTeamKey).getOpptGamesWon() + " - " + standingsMap.get(opptTeamKey).getOpptGamesPlayed() + " minus " +
				headToHeadMap.get(opptTeamKey).getOpptGamesWon() + " - " + headToHeadMap.get(opptTeamKey).getOpptGamesPlayed());

			if (opptGamesWon > opptGamesPlayed) {
				//head to head wins exceed opponent wins, should only occur until wins start to occur
				//1. observed occurrence when loading standings before entire day's games were loaded
				//2. observed occurrence when to first game played on losing team
				logger.info('\n' + "Crazy opptGamesWon more than opptGamesPlayed!");
				opptGamesWon = opptGamesPlayed;
			}
		}

		BigDecimal opptRecord = opptGamesPlayed == 0 ? new BigDecimal(0) : new BigDecimal(opptGamesWon).divide(new BigDecimal(opptGamesPlayed), 4, RoundingMode.HALF_UP);
		BigDecimal opptOpptRecord = opptOpptGamesWon == 0 ? new BigDecimal(0) : new BigDecimal(opptOpptGamesWon).divide(new BigDecimal(opptOpptGamesPlayed), 4, RoundingMode.HALF_UP);
		logger.debug('\n' + "  Opponent Games Won/Played = " + opptGamesWon + "-" + opptGamesPlayed);
		logger.debug('\n' + "  OpptOppt Games Won/Played = " + opptOpptGamesWon + "-" + opptOpptGamesPlayed);
		logger.debug('\n' + "  Opponent Record = " + opptRecord);
		logger.debug('\n' + "  OpptOppt Record = " + opptOpptRecord);
		logger.debug('\n' + "  Strength Of Schedule " + teamKey + " " + opptRecord.multiply(new BigDecimal(2)).add(opptOpptRecord).divide(new BigDecimal(3), 4, RoundingMode.HALF_UP));

		return new StandingRecord(opptGamesWon, opptGamesPlayed, opptOpptGamesWon, opptOpptGamesPlayed);
	}
}