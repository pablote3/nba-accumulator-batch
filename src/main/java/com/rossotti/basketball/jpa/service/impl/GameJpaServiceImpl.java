package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.BoxScore;
import com.rossotti.basketball.jpa.model.BoxScoreStats;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.GameOfficial;
import com.rossotti.basketball.jpa.repository.GameRepository;
import com.rossotti.basketball.jpa.service.GameJpaService;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameJpaServiceImpl implements GameJpaService {

	private GameRepository gameRepository;

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public Game findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate) {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate(teamKey, DateTimeConverter.getLocalDateTimeMin(asOfDate), DateTimeConverter.getLocalDateTimeMax(asOfDate));
		if (game != null) {
			game.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			game = new Game(StatusCodeDAO.NotFound);
		}
		return game;
	}

	@Override
	public List<Game> findByTeamKeyAndAsOfDateSeason(String teamKey, LocalDate asOfDate) {
		return gameRepository.findByTeamKeyAndFromDateAndToDateSeason(teamKey, DateTimeConverter.getLocalDateTimeSeasonMin(asOfDate), DateTimeConverter.getLocalDateTimeMax(asOfDate));
	}

	@Override
	public List<Game> findByAsOfDate(LocalDate asOfDate) {
		return gameRepository.findByFromDateAndToDate(DateTimeConverter.getLocalDateTimeMin(asOfDate), DateTimeConverter.getLocalDateTimeMax(asOfDate));
	}

	@Override
	public int findCountByAsOfDate(LocalDate asOfDate) {
		return gameRepository.findCountByFromDateAndToDate(DateTimeConverter.getLocalDateTimeMin(asOfDate), DateTimeConverter.getLocalDateTimeMax(asOfDate));
	}

	@Override
	public LocalDateTime findPreviousByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate) {
		List<LocalDateTime> gameDateTime = gameRepository.findPreviousByTeamKeyAndAsOfDate(teamKey, DateTimeConverter.getLocalDateTimeMin(asOfDate));
		if (gameDateTime != null && gameDateTime.size() > 0) {
			return gameDateTime.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	public Game getById(Long id) {
		return gameRepository.findOne(id);
	}

	@Override
	public List<?> listAll() {
		List<Game> games = new ArrayList<>();
		gameRepository.findAll().forEach(games::add);
		return games;
	}

	@Override
	public Game create(Game createGame) {
		Game game = findByTeamKeyAndAsOfDate(createGame.getBoxScoreAway().getTeam().getTeamKey(), DateTimeConverter.getLocalDate(createGame.getGameDateTime()));
		if (game.isNotFound()) {
			gameRepository.save(createGame);
			createGame.setStatusCode(StatusCodeDAO.Created);
			return createGame;
		}
		else {
			return game;
		}
	}

	@Override
	public Game update(Game updateGame) {
		Game findGame = findByTeamKeyAndAsOfDate(updateGame.getBoxScoreAway().getTeam().getTeamKey(), DateTimeConverter.getLocalDate(updateGame.getGameDateTime()));
		if (findGame.isFound()) {
			if (!findGame.getBoxScoreHome().getTeam().getTeamKey().equals(updateGame.getBoxScoreHome().getTeam().getTeamKey())) {
				findGame.setStatusCode(StatusCodeDAO.NotFound);
				return findGame;
			}
			findGame.setStatus(updateGame.getStatus());
			for (int i = 0; i < updateGame.getGameOfficials().size(); i++) {
				GameOfficial gameOfficial = updateGame.getGameOfficials().get(i);
				gameOfficial.setGame(findGame);
				findGame.addGameOfficial(gameOfficial);
			}
			BoxScore findHomeBoxScore = findGame.getBoxScoreHome();
			BoxScore updateHomeBoxScore = updateGame.getBoxScoreHome();
			findHomeBoxScore.setResult(updateHomeBoxScore.getResult());
			findHomeBoxScore.setBoxScoreStats(new BoxScoreStats());
			findHomeBoxScore.getBoxScoreStats().setMinutes(updateHomeBoxScore.getBoxScoreStats().getMinutes());
			findHomeBoxScore.getBoxScoreStats().setPoints(updateHomeBoxScore.getBoxScoreStats().getPoints());
			findHomeBoxScore.setPointsPeriod1(updateHomeBoxScore.getPointsPeriod1());
			findHomeBoxScore.setPointsPeriod2(updateHomeBoxScore.getPointsPeriod2());
			findHomeBoxScore.setPointsPeriod3(updateHomeBoxScore.getPointsPeriod3());
			findHomeBoxScore.setPointsPeriod4(updateHomeBoxScore.getPointsPeriod4());
			findHomeBoxScore.setPointsPeriod5(updateHomeBoxScore.getPointsPeriod5());
			findHomeBoxScore.setPointsPeriod6(updateHomeBoxScore.getPointsPeriod6());
			findHomeBoxScore.setPointsPeriod7(updateHomeBoxScore.getPointsPeriod7());
			findHomeBoxScore.setPointsPeriod8(updateHomeBoxScore.getPointsPeriod8());
			findHomeBoxScore.getBoxScoreStats().setAssists(updateHomeBoxScore.getBoxScoreStats().getAssists());
			findHomeBoxScore.getBoxScoreStats().setTurnovers(updateHomeBoxScore.getBoxScoreStats().getTurnovers());
			findHomeBoxScore.getBoxScoreStats().setSteals(updateHomeBoxScore.getBoxScoreStats().getSteals());
			findHomeBoxScore.getBoxScoreStats().setBlocks(updateHomeBoxScore.getBoxScoreStats().getBlocks());
			findHomeBoxScore.getBoxScoreStats().setFieldGoalAttempts(updateHomeBoxScore.getBoxScoreStats().getFieldGoalAttempts());
			findHomeBoxScore.getBoxScoreStats().setFieldGoalMade(updateHomeBoxScore.getBoxScoreStats().getFieldGoalMade());
			findHomeBoxScore.getBoxScoreStats().setFieldGoalPercent(updateHomeBoxScore.getBoxScoreStats().getFieldGoalPercent());
			findHomeBoxScore.getBoxScoreStats().setThreePointAttempts(updateHomeBoxScore.getBoxScoreStats().getThreePointAttempts());
			findHomeBoxScore.getBoxScoreStats().setThreePointMade(updateHomeBoxScore.getBoxScoreStats().getThreePointMade());
			findHomeBoxScore.getBoxScoreStats().setThreePointPercent(updateHomeBoxScore.getBoxScoreStats().getThreePointPercent());
			findHomeBoxScore.getBoxScoreStats().setFreeThrowAttempts(updateHomeBoxScore.getBoxScoreStats().getFreeThrowAttempts());
			findHomeBoxScore.getBoxScoreStats().setFreeThrowMade(updateHomeBoxScore.getBoxScoreStats().getFreeThrowMade());
			findHomeBoxScore.getBoxScoreStats().setFreeThrowPercent(updateHomeBoxScore.getBoxScoreStats().getFreeThrowPercent());
			findHomeBoxScore.getBoxScoreStats().setReboundsOffense(updateHomeBoxScore.getBoxScoreStats().getReboundsOffense());
			findHomeBoxScore.getBoxScoreStats().setReboundsDefense(updateHomeBoxScore.getBoxScoreStats().getReboundsDefense());
			findHomeBoxScore.getBoxScoreStats().setPersonalFouls(updateHomeBoxScore.getBoxScoreStats().getPersonalFouls());
			findHomeBoxScore.setDaysOff(updateHomeBoxScore.getDaysOff());
			findHomeBoxScore.setBoxScorePlayers(updateHomeBoxScore.getBoxScorePlayers());
			for (int i = 0; i < findHomeBoxScore.getBoxScorePlayers().size(); i++) {
				findHomeBoxScore.getBoxScorePlayers().get(i).setBoxScore(findHomeBoxScore);
			}

			BoxScore findAwayBoxScore = findGame.getBoxScoreAway();
			BoxScore updateAwayBoxScore = updateGame.getBoxScoreAway();
			findAwayBoxScore.setResult(updateAwayBoxScore.getResult());
			findAwayBoxScore.setBoxScoreStats(new BoxScoreStats());
			findAwayBoxScore.getBoxScoreStats().setMinutes(updateAwayBoxScore.getBoxScoreStats().getMinutes());
			findAwayBoxScore.getBoxScoreStats().setPoints(updateAwayBoxScore.getBoxScoreStats().getPoints());
			findAwayBoxScore.setPointsPeriod1(updateAwayBoxScore.getPointsPeriod1());
			findAwayBoxScore.setPointsPeriod2(updateAwayBoxScore.getPointsPeriod2());
			findAwayBoxScore.setPointsPeriod3(updateAwayBoxScore.getPointsPeriod3());
			findAwayBoxScore.setPointsPeriod4(updateAwayBoxScore.getPointsPeriod4());
			findAwayBoxScore.setPointsPeriod5(updateAwayBoxScore.getPointsPeriod5());
			findAwayBoxScore.setPointsPeriod6(updateAwayBoxScore.getPointsPeriod6());
			findAwayBoxScore.setPointsPeriod7(updateAwayBoxScore.getPointsPeriod7());
			findAwayBoxScore.setPointsPeriod8(updateAwayBoxScore.getPointsPeriod8());
			findAwayBoxScore.getBoxScoreStats().setAssists(updateAwayBoxScore.getBoxScoreStats().getAssists());
			findAwayBoxScore.getBoxScoreStats().setTurnovers(updateAwayBoxScore.getBoxScoreStats().getTurnovers());
			findAwayBoxScore.getBoxScoreStats().setSteals(updateAwayBoxScore.getBoxScoreStats().getSteals());
			findAwayBoxScore.getBoxScoreStats().setBlocks(updateAwayBoxScore.getBoxScoreStats().getBlocks());
			findAwayBoxScore.getBoxScoreStats().setFieldGoalAttempts(updateAwayBoxScore.getBoxScoreStats().getFieldGoalAttempts());
			findAwayBoxScore.getBoxScoreStats().setFieldGoalMade(updateAwayBoxScore.getBoxScoreStats().getFieldGoalMade());
			findAwayBoxScore.getBoxScoreStats().setFieldGoalPercent(updateAwayBoxScore.getBoxScoreStats().getFieldGoalPercent());
			findAwayBoxScore.getBoxScoreStats().setThreePointAttempts(updateAwayBoxScore.getBoxScoreStats().getThreePointAttempts());
			findAwayBoxScore.getBoxScoreStats().setThreePointMade(updateAwayBoxScore.getBoxScoreStats().getThreePointMade());
			findAwayBoxScore.getBoxScoreStats().setThreePointPercent(updateAwayBoxScore.getBoxScoreStats().getThreePointPercent());
			findAwayBoxScore.getBoxScoreStats().setFreeThrowAttempts(updateAwayBoxScore.getBoxScoreStats().getFreeThrowAttempts());
			findAwayBoxScore.getBoxScoreStats().setFreeThrowMade(updateAwayBoxScore.getBoxScoreStats().getFreeThrowMade());
			findAwayBoxScore.getBoxScoreStats().setFreeThrowPercent(updateAwayBoxScore.getBoxScoreStats().getFreeThrowPercent());
			findAwayBoxScore.getBoxScoreStats().setReboundsOffense(updateAwayBoxScore.getBoxScoreStats().getReboundsOffense());
			findAwayBoxScore.getBoxScoreStats().setReboundsDefense(updateAwayBoxScore.getBoxScoreStats().getReboundsDefense());
			findAwayBoxScore.getBoxScoreStats().setPersonalFouls(updateAwayBoxScore.getBoxScoreStats().getPersonalFouls());
			findAwayBoxScore.setDaysOff(updateAwayBoxScore.getDaysOff());
			findAwayBoxScore.setBoxScorePlayers(updateAwayBoxScore.getBoxScorePlayers());
			for (int i = 0; i < findAwayBoxScore.getBoxScorePlayers().size(); i++) {
				findAwayBoxScore.getBoxScorePlayers().get(i).setBoxScore(findAwayBoxScore);
			}
			gameRepository.save(findGame);
			findGame.setStatusCode(StatusCodeDAO.Updated);
		}
		return findGame;
	}

	@Override
	public Game delete(Long id) {
		Game findGame = getById(id);
		if (findGame != null && findGame.isFound()) {
			gameRepository.delete(findGame.getId());
			findGame.setStatusCode(StatusCodeDAO.Deleted);
			return findGame;
		}
		else {
			return new Game(StatusCodeDAO.NotFound);
		}
	}
}