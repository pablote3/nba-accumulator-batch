package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TeamRepository extends Repository<Team, Long> {

	List<Team> findAll();

	Team findOne(Long id);

	Team save(Team team);

	void delete(Long id);

	String findByTeamKeyAndFromDateAndToDate =
			"from Team " +
			"where teamKey = :teamKey " +
			"and fromDate <= :fromDate " +
			"and toDate >= :toDate";

	@Query(findByTeamKeyAndFromDateAndToDate)
	Team findByTeamKeyAndFromDateAndToDate(@Param("teamKey") String teamKey, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	String findByLastNameAndFromDateAndToDate =
			"from Team " +
			"where lastName = :lastName " +
			"and fromDate <= :fromDate " +
			"and toDate >= :toDate";

	@Query(findByLastNameAndFromDateAndToDate)
	Team findByLastNameAndFromDateAndToDate(@Param("lastName") String lastName, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	List<Team> findByTeamKey(String teamKey);

	String findByFromDateAndToDate =
			"from Team " +
			"where fromDate <= :fromDate " +
			"and toDate >= :toDate";

	@Query(findByFromDateAndToDate)
	List<Team> findByFromDateAndToDate(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}