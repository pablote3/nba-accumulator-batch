package com.rossotti.basketball.app;

import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.GameOfficial;
import com.rossotti.basketball.jpa.model.Official;
import com.rossotti.basketball.jpa.service.OfficialJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfficialAppService {
	private final OfficialJpaService officialJpaService;

	private final Logger logger = LoggerFactory.getLogger(OfficialAppService.class);

	@Autowired
	public OfficialAppService(OfficialJpaService officialJpaService) {
		this.officialJpaService = officialJpaService;
	}

	public List<GameOfficial> getGameOfficials(OfficialDTO[] officials, LocalDate gameDate) {
		List<GameOfficial> gameOfficials = new ArrayList<>();
		for (OfficialDTO official1 : officials) {
			String lastName = official1.getLast_name();
			String firstName = official1.getFirst_name();
			Official official = officialJpaService.findByLastNameAndFirstNameAndAsOfDate(lastName, firstName, gameDate);
			if (official.isNotFound()) {
				logger.info("Official not found " + firstName + " " + lastName);
				throw new NoSuchEntityException(Official.class);
			} else {
				GameOfficial gameOfficial = new GameOfficial();
				gameOfficial.setOfficial(official);
				gameOfficials.add(gameOfficial);
			}
		}
		return gameOfficials;
	}
}