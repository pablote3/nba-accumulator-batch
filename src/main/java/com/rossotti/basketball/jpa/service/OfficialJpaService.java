package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Official;
import org.springframework.stereotype.Service;

import org.joda.time.LocalDate;
import java.util.List;

@Service
public interface OfficialJpaService extends CrudService<Official> {
	Official findByLastNameAndFirstNameAndAsOfDate(String lastName, String firstName, LocalDate asOfDate);
	List<Official> findByAsOfDate(LocalDate asOfDate);
}