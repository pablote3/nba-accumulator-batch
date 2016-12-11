package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.RosterPlayer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RosterPlayerService extends CrudService<RosterPlayer> {

	RosterPlayer findByLastNameAndFirstNameAndBirthdateAndAsOfDate(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate);

	RosterPlayer findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(String lastName, String firstName, String teamKey, LocalDate asOfDate);

	List<RosterPlayer> findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate);

	List<RosterPlayer> findByLastNameAndFirstNameAndBirthdate(String lastName, String firstName, LocalDate birthdate);
}