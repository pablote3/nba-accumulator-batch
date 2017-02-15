package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Player;
import org.springframework.stereotype.Service;

import org.joda.time.LocalDate;
import java.util.List;

@Service
public interface PlayerJpaService extends CrudService<Player> {
	List<Player> findByLastNameAndFirstName(String lastName, String firstName);
	Player findByLastNameAndFirstNameAndBirthdate(String lastName, String firstName, LocalDate birthdate);
}