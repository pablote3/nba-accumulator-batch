package com.rossotti.basketball.app;

import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.service.TeamJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TeamAppService {
	private final TeamJpaService teamJpaService;

	private final Logger logger = LoggerFactory.getLogger(TeamAppService.class);

	@Autowired
	public TeamAppService(TeamJpaService teamJpaService) {
		this.teamJpaService = teamJpaService;
	}

	public Team findTeamByTeamKey(String teamKey, LocalDate gameDate) {
		Team team = teamJpaService.findByTeamKeyAndAsOfDate(teamKey, gameDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + teamKey);
			throw new NoSuchEntityException(Team.class);
		}
		return team;
	}

	public Team findTeamByLastName(String lastName, LocalDate gameDate) {
		Team team = teamJpaService.findByLastNameAndAsOfDate(lastName, gameDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + lastName);
			throw new NoSuchEntityException(Team.class);
		}
		return team;
	}
}