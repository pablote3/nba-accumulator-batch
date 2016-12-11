package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.repository.TeamRepository;
import com.rossotti.basketball.jpa.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

	private TeamRepository teamRepository;

	@Autowired
	public void setTeamRepository(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Override
	public List<Team> findByTeamKey(String teamKey) {
		return teamRepository.findByTeamKey(teamKey);
	}

	@Override
	public List<Team> findByDate(LocalDate asOfDate) {
		return teamRepository.findByFromDateAndToDate(asOfDate, asOfDate);
	}

	@Override
	public Team findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate) {
		Team team = teamRepository.findByTeamKeyAndFromDateAndToDate(teamKey, asOfDate, asOfDate);
		if (team != null) {
			team.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			team = new Team(StatusCodeDAO.NotFound);
		}
		return team;
	}

	@Override
	public Team findByLastNameAndAsOfDate(String lastName, LocalDate asOfDate) {
		Team team = teamRepository.findByLastNameAndFromDateAndToDate(lastName, asOfDate, asOfDate);
		if (team != null) {
			team.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			team = new Team(StatusCodeDAO.NotFound);
		}
		return team;
	}

	@Override
	public List<?> listAll() {
		List<Team> teams = new ArrayList<>();
		teamRepository.findAll().forEach(teams::add);
		return teams;
	}

	@Override
	public Team getById(Long id) {
		return teamRepository.findOne(id);
	}

	@Override
	public Team create(Team createTeam) {
		Team team = findByTeamKeyAndAsOfDate(createTeam.getTeamKey(), createTeam.getFromDate());
		if (team.isNotFound()) {
			teamRepository.save(createTeam);
			createTeam.setStatusCode(StatusCodeDAO.Created);
			return createTeam;
		}
		else {
			return team;
		}
	}

	@Override
	public Team update(Team updateTeam) {
		Team team = findByTeamKeyAndAsOfDate(updateTeam.getTeamKey(), updateTeam.getFromDate());
		if (team.isFound()) {
			team.setLastName(updateTeam.getLastName());
			team.setFirstName(updateTeam.getFirstName());
			team.setFullName(updateTeam.getFullName());
			team.setAbbr(updateTeam.getAbbr());
			team.setFromDate(updateTeam.getFromDate());
			team.setToDate(updateTeam.getToDate());
			team.setConference(updateTeam.getConference());
			team.setDivision(updateTeam.getDivision());
			team.setCity(updateTeam.getCity());
			team.setState(updateTeam.getState());
			team.setSiteName(updateTeam.getSiteName());
			teamRepository.save(team);
			team.setStatusCode(StatusCodeDAO.Updated);
		}
		return team;
	}

	@Override
	public Team delete(Long id) {
		Team findTeam = getById(id);
		if (findTeam != null && findTeam.isFound()) {
			teamRepository.delete(findTeam.getId());
			findTeam.setStatusCode(StatusCodeDAO.Deleted);
			return findTeam;
		}
		else {
			return new Team(StatusCodeDAO.NotFound);
		}
	}
}