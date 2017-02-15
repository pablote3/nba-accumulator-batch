package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Game;
import org.springframework.stereotype.Service;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import java.util.List;

@Service
public interface GameJpaService extends CrudService<Game> {
	Game findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate);
	List<Game> findByTeamKeyAndAsOfDateSeason(String teamKey, LocalDate asOfDate);
	List<Game> findByAsOfDate(LocalDate asOfDate);
	int findCountByAsOfDate(LocalDate asOfDate);
	LocalDateTime findPreviousByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate);
}