package com.rossotti.basketball.app.service;

import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.jpa.model.RosterPlayer.Position;
import com.rossotti.basketball.jpa.service.RosterPlayerJpaService;
import com.rossotti.basketball.jpa.service.TeamJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RosterPlayerAppService {
	private final RosterPlayerJpaService rosterPlayerJpaService;

	private final TeamJpaService teamJpaService;

	private final Logger logger = LoggerFactory.getLogger(RosterPlayerAppService.class);

	@Autowired
	public RosterPlayerAppService(RosterPlayerJpaService rosterPlayerJpaService, TeamJpaService teamJpaService) {
		this.rosterPlayerJpaService = rosterPlayerJpaService;
		this.teamJpaService = teamJpaService;
	}

	public List<BoxScorePlayer> getBoxScorePlayers(BoxScorePlayerDTO[] boxScorePlayerDTOs, BoxScore boxScore, LocalDate asOfDate, String teamKey) {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<>();
		for (BoxScorePlayerDTO boxScorePlayerDTO : boxScorePlayerDTOs) {
			String lastName = boxScorePlayerDTO.getLast_name();
			String firstName = boxScorePlayerDTO.getFirst_name();
			RosterPlayer rosterPlayer = rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(lastName, firstName, teamKey, asOfDate);
			if (rosterPlayer.isNotFound()) {
				logger.info("Roster Player not found " + firstName + " " + lastName + " Team: " + teamKey + " AsOfDate: " + asOfDate);
				throw new NoSuchEntityException(RosterPlayer.class);
			} else {
				BoxScorePlayer boxScorePlayer = new BoxScorePlayer();
				boxScorePlayer.setBoxScore(boxScore);
				boxScorePlayer.setBoxScoreStats(new BoxScoreStats());
				boxScorePlayer.setRosterPlayer(rosterPlayer);
				boxScorePlayer.setPosition(Position.valueOf(boxScorePlayerDTO.getPosition()));
				boxScorePlayer.setStarter(boxScorePlayerDTO.getIs_starter());
				BoxScoreStats boxScoreStats = boxScorePlayer.getBoxScoreStats();
				boxScoreStats.setMinutes(boxScorePlayerDTO.getMinutes());
				boxScoreStats.setPoints(boxScorePlayerDTO.getPoints());
				boxScoreStats.setAssists(boxScorePlayerDTO.getAssists());
				boxScoreStats.setTurnovers(boxScorePlayerDTO.getTurnovers());
				boxScoreStats.setSteals(boxScorePlayerDTO.getSteals());
				boxScoreStats.setBlocks(boxScorePlayerDTO.getBlocks());
				boxScoreStats.setFieldGoalAttempts(boxScorePlayerDTO.getField_goals_attempted());
				boxScoreStats.setFieldGoalMade(boxScorePlayerDTO.getField_goals_made());
				boxScoreStats.setFieldGoalPercent(boxScorePlayerDTO.getField_goal_percentage());
				boxScoreStats.setThreePointAttempts(boxScorePlayerDTO.getThree_point_field_goals_attempted());
				boxScoreStats.setThreePointMade(boxScorePlayerDTO.getThree_point_field_goals_made());
				boxScoreStats.setThreePointPercent(boxScorePlayerDTO.getThree_point_percentage());
				boxScoreStats.setFreeThrowAttempts(boxScorePlayerDTO.getFree_throws_attempted());
				boxScoreStats.setFreeThrowMade(boxScorePlayerDTO.getFree_throws_made());
				boxScoreStats.setFreeThrowPercent(boxScorePlayerDTO.getFree_throw_percentage());
				boxScoreStats.setReboundsOffense(boxScorePlayerDTO.getOffensive_rebounds());
				boxScoreStats.setReboundsDefense(boxScorePlayerDTO.getDefensive_rebounds());
				boxScoreStats.setPersonalFouls(boxScorePlayerDTO.getPersonal_fouls());
				boxScorePlayers.add(boxScorePlayer);
			}
		}
		return boxScorePlayers;
	}

	public List<RosterPlayer> getRosterPlayers(RosterPlayerDTO[] rosterPlayerDTOs, LocalDate asOfDate, String teamKey) {
		List<RosterPlayer> rosterPlayers = new ArrayList<>();
		Team team = teamJpaService.findByTeamKeyAndAsOfDate(teamKey, asOfDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + teamKey);
			throw new NoSuchEntityException(Team.class);
		}
		else {
			for (RosterPlayerDTO rosterPlayerDTO : rosterPlayerDTOs) {
				Player player = new Player();
				player.setLastName(rosterPlayerDTO.getLast_name());
				player.setFirstName(rosterPlayerDTO.getFirst_name());
				player.setDisplayName(rosterPlayerDTO.getDisplay_name());
				player.setHeight(rosterPlayerDTO.getHeight_in());
				player.setWeight(rosterPlayerDTO.getWeight_lb());
				player.setBirthdate(rosterPlayerDTO.getBirthdate());
				player.setBirthplace(rosterPlayerDTO.getBirthplace());
				RosterPlayer rosterPlayer = new RosterPlayer();
				rosterPlayer.setPlayer(player);
				rosterPlayer.setTeam(team);
				rosterPlayer.setNumber(rosterPlayerDTO.getUniform_number());
				rosterPlayer.setPosition(Position.valueOf(rosterPlayerDTO.getPosition()));
				rosterPlayers.add(rosterPlayer);
			}
		}
		return rosterPlayers;
	}

	public RosterPlayer findByPlayerNameTeamAsOfDate(String lastName, String firstName, String teamKey, LocalDate asOfDate) {
		return rosterPlayerJpaService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(lastName, firstName, teamKey, asOfDate);
	}

	public RosterPlayer findByPlayerNameBirthdateAsOfDate(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate) {
		return rosterPlayerJpaService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate(lastName, firstName, birthdate, asOfDate);
	}

	public List<RosterPlayer> findByTeamKeyAsOfDate(LocalDate asOfDate, String teamKey) {
		return rosterPlayerJpaService.findByTeamKeyAndAsOfDate(teamKey, asOfDate);
	}

	public RosterPlayer createRosterPlayer(RosterPlayer rosterPlayer) {
		return rosterPlayerJpaService.create(rosterPlayer);
	}

	public RosterPlayer updateRosterPlayer(RosterPlayer rosterPlayer) {
		return rosterPlayerJpaService.update(rosterPlayer);
	}
}