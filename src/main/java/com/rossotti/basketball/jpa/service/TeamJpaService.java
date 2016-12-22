package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Team;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TeamJpaService extends CrudService<Team> {
	List<Team> findByTeamKey(String teamKey);
	List<Team> findByDate(LocalDate asOfDate);
	Team findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate);
	Team findByLastNameAndAsOfDate(String lastName, LocalDate asOfDate);
}