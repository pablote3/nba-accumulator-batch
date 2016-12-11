package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Game;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface GameService extends CrudService<Game> {
	Game findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate);
	List<Game> findByTeamKeyAndAsOfDateSeason(String teamKey, LocalDate asOfDate);
	List<Game> findByAsOfDate(LocalDate asOfDate);
	LocalDateTime findPreviousByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate);
}