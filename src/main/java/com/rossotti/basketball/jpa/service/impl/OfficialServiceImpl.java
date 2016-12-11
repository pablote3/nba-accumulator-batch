package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Official;
import com.rossotti.basketball.jpa.repository.OfficialRepository;
import com.rossotti.basketball.jpa.service.OfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfficialServiceImpl implements OfficialService {

	private OfficialRepository officialRepository;

	@Autowired
	public void setOfficialRepository(OfficialRepository officialRepository) {
		this.officialRepository = officialRepository;
	}

	@Override
	public Official findByLastNameAndFirstNameAndAsOfDate(String lastName, String firstName, LocalDate asOfDate) {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate(lastName, firstName, asOfDate, asOfDate);
		if (official != null) {
			official.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			official = new Official(StatusCodeDAO.NotFound);
		}
		return official;
	}

	@Override
	public List<Official> findByAsOfDate(LocalDate asOfDate) {
		return officialRepository.findByFromDateAndToDate(asOfDate, asOfDate);
	}

	@Override
	public List<?> listAll() {
		List<Official> teams = new ArrayList<>();
		officialRepository.findAll().forEach(teams::add);
		return teams;
	}

	@Override
	public Official getById(Long id) {
		return officialRepository.findOne(id);
	}

	@Override
	public Official create(Official createOfficial) {
		Official official = findByLastNameAndFirstNameAndAsOfDate(createOfficial.getLastName(), createOfficial.getFirstName(), createOfficial.getFromDate());
		if (official.isNotFound()) {
			officialRepository.save(createOfficial);
			createOfficial.setStatusCode(StatusCodeDAO.Created);
			return createOfficial;
		}
		else {
			return official;
		}
	}

	@Override
	public Official update(Official updateOfficial) {
		Official official = findByLastNameAndFirstNameAndAsOfDate(updateOfficial.getLastName(), updateOfficial.getFirstName(), updateOfficial.getFromDate());
		if (official.isFound()) {
			official.setLastName(updateOfficial.getLastName());
			official.setFirstName(updateOfficial.getFirstName());
			official.setFromDate(updateOfficial.getFromDate());
			official.setToDate(updateOfficial.getToDate());
			official.setNumber(updateOfficial.getNumber());
			officialRepository.save(official);
			official.setStatusCode(StatusCodeDAO.Updated);
		}
		return official;
	}

	@Override
	public Official delete(Long id) {
		Official findOfficial = getById(id);
		if (findOfficial != null && findOfficial.isFound()) {
			officialRepository.delete(findOfficial.getId());
			findOfficial.setStatusCode(StatusCodeDAO.Deleted);
			return findOfficial;
		}
		else {
			return new Official(StatusCodeDAO.NotFound);
		}
	}
}