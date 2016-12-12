package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.RosterPlayer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RosterPlayerRepository extends Repository<RosterPlayer, Long> {

	List<RosterPlayer> findAll();

	RosterPlayer findOne(Long id);

	void save(RosterPlayer player);

	void delete(Long id);

	String findByTeamKeyAndAsOfDate =
			"select rp from RosterPlayer rp " +
			"inner join rp.team t " +
			"where t.teamKey = :teamKey " +
			"and rp.fromDate <= :asOfDate " +
			"and rp.toDate >= :asOfDate";

	@Query(findByTeamKeyAndAsOfDate)
	List<RosterPlayer> findByTeamKeyAndAsOfDate(@Param("teamKey") String teamKey, @Param("asOfDate") LocalDate asOfDate);

	String findByLastNameAndFirstNameAndBirthdate =
			"select rp from RosterPlayer rp " +
			"inner join rp.player p " +
			"where p.lastName = :lastName " +
			"and p.firstName = :firstName " +
			"and p.birthdate = :birthdate ";

	@Query(findByLastNameAndFirstNameAndBirthdate)
	List<RosterPlayer> findByLastNameAndFirstNameAndBirthdate(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("birthdate") LocalDate birthdate);

	String findByLastNameAndFirstNameAndBirthdateAndAsOfDate =
			"select rp from RosterPlayer rp " +
			"inner join rp.player p " +
			"where p.lastName = :lastName " +
			"and p.firstName = :firstName " +
			"and p.birthdate = :birthdate " +
			"and rp.fromDate <= :asOfDate " +
			"and rp.toDate >= :asOfDate";

	@Query(findByLastNameAndFirstNameAndBirthdateAndAsOfDate)
	RosterPlayer findByLastNameAndFirstNameAndBirthdateAndAsOfDate(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("birthdate") LocalDate birthdate, @Param("asOfDate") LocalDate asOfDate);

	String findByLastNameAndFirstNameAndTeamKeyAndAsOfDate =
			"select rp from RosterPlayer rp " +
			"inner join rp.player p " +
			"inner join rp.team t " +
			"where p.lastName = :lastName " +
			"and p.firstName = :firstName " +
			"and t.teamKey = :teamKey " +
			"and rp.fromDate <= :asOfDate " +
			"and rp.toDate >= :asOfDate";

	@Query(findByLastNameAndFirstNameAndTeamKeyAndAsOfDate)
	RosterPlayer findByLastNameAndFirstNameAndTeamKeyAndAsOfDate(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("teamKey") String teamKey, @Param("asOfDate") LocalDate asOfDate);
}