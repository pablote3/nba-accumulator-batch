package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.jpa.repository.StandingRepository;
import com.rossotti.basketball.jpa.service.StandingJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StandingJpaServiceImpl implements StandingJpaService {

	private StandingRepository standingRepository;

	@Autowired
	public void setStandingRepository(StandingRepository standingRepository) {
		this.standingRepository = standingRepository;
	}

	@Override
	public List<Standing> findByTeamKey(String teamKey) {
		return standingRepository.findByTeamKey(teamKey);
	}

	@Override
	public List<Standing> findByAsOfDate(LocalDate asOfDate) {
		return standingRepository.findByStandingDate(asOfDate);
	}

	@Override
	public Standing findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate) {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate(teamKey, asOfDate);
		if (standing != null) {
			standing.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			standing = new Standing(StatusCodeDAO.NotFound);
		}
		return standing;
	}

	@Override
	public List<?> listAll() {
		List<Standing> standings = new ArrayList<>();
		standingRepository.findAll().forEach(standings::add);
		return standings;
	}

	@Override
	public Standing getById(Long id) {
		return standingRepository.findOne(id);
	}

	@Override
	public Standing create(Standing createStanding) {
		Standing standing = findByTeamKeyAndAsOfDate(createStanding.getTeam().getTeamKey(), createStanding.getStandingDate());
		if (standing.isNotFound()) {
			standingRepository.save(createStanding);
			createStanding.setStatusCode(StatusCodeDAO.Created);
			return createStanding;
		}
		else {
			return standing;
		}
	}

	@Override
	public Standing update(Standing updateStanding) {
		Standing standing = findByTeamKeyAndAsOfDate(updateStanding.getTeam().getTeamKey(), updateStanding.getStandingDate());
		if (standing.isFound()) {
			standing.setRank(updateStanding.getRank());
			standing.setOrdinalRank(updateStanding.getOrdinalRank());
			standing.setGamesWon(updateStanding.getGamesWon());
			standing.setGamesLost(updateStanding.getGamesLost());
			standing.setStreak(updateStanding.getStreak());
			standing.setStreakType(updateStanding.getStreakType());
			standing.setStreakTotal(updateStanding.getStreakTotal());
			standing.setGamesBack(updateStanding.getGamesBack());
			standing.setPointsFor(updateStanding.getPointsFor());
			standing.setPointsAgainst(updateStanding.getPointsAgainst());
			standing.setHomeWins(updateStanding.getHomeWins());
			standing.setHomeLosses(updateStanding.getHomeLosses());
			standing.setAwayWins(updateStanding.getAwayWins());
			standing.setAwayLosses(updateStanding.getAwayLosses());
			standing.setConferenceWins(updateStanding.getConferenceWins());
			standing.setConferenceLosses(updateStanding.getConferenceLosses());
			standing.setLastFive(updateStanding.getLastFive());
			standing.setLastTen(updateStanding.getLastTen());
			standing.setGamesPlayed(updateStanding.getGamesPlayed());
			standing.setPointsScoredPerGame(updateStanding.getPointsScoredPerGame());
			standing.setPointsAllowedPerGame(updateStanding.getPointsAllowedPerGame());
			standing.setWinPercentage(updateStanding.getWinPercentage());
			standing.setPointDifferential(updateStanding.getPointDifferential());
			standing.setPointDifferentialPerGame(updateStanding.getPointDifferentialPerGame());
			standing.setOpptGamesWon(updateStanding.getOpptGamesWon());
			standing.setOpptGamesPlayed(updateStanding.getOpptGamesPlayed());
			standing.setOpptOpptGamesWon(updateStanding.getOpptOpptGamesWon());
			standing.setOpptOpptGamesPlayed(updateStanding.getOpptOpptGamesPlayed());
			standingRepository.save(standing);
			standing.setStatusCode(StatusCodeDAO.Updated);
		}
		return standing;
	}

	@Override
	public Standing delete(Long id) {
		Standing findStanding = getById(id);
		if (findStanding != null && findStanding.isFound()) {
			standingRepository.delete(findStanding.getId());
			findStanding.setStatusCode(StatusCodeDAO.Deleted);
			return findStanding;
		}
		else {
			return new Standing(StatusCodeDAO.NotFound);
		}
	}
}