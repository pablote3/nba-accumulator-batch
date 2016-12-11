package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Official;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OfficialRepository extends Repository<Official, Long> {

	List<Official> findAll();

	Official findOne(Long id);

	Official save(Official official);

	void delete(Long id);

	String findByLastNameAndFirstNameAndFromDateAndToDate =
			"from Official " +
			"where lastName = :lastName " +
			"and firstName = :firstName " +
			"and fromDate <= :fromDate " +
			"and toDate >= :toDate";

	@Query(findByLastNameAndFirstNameAndFromDateAndToDate)
	Official findByLastNameAndFirstNameAndFromDateAndToDate(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	String findByFromDateAndToDate =
			"from Official " +
			"where fromDate <= :fromDate " +
			"and toDate >= :toDate";

	@Query(findByFromDateAndToDate)
	List<Official> findByFromDateAndToDate(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}