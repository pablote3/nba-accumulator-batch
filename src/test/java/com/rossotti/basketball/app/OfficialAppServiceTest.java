package com.rossotti.basketball.app;

import com.rossotti.basketball.app.service.OfficialAppService;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.jpa.exception.NoSuchEntityException;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.GameOfficial;
import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Official;
import com.rossotti.basketball.jpa.service.OfficialJpaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.joda.time.LocalDate;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficialAppServiceTest {
	@Mock
	private OfficialJpaService officialJpaService;

	@InjectMocks
	private OfficialAppService officialAppService;

	@Test(expected=NoSuchEntityException.class)
	public void getGameOfficials_notFound() {
		when(officialJpaService.findByLastNameAndFirstNameAndAsOfDate(anyString(), anyString(), anyObject()))
			.thenReturn(createMockOfficial("", "", StatusCodeDAO.NotFound));
		List<GameOfficial> officials = officialAppService.getGameOfficials(createMockOfficialDTOs(), createMockGame(), new LocalDate(1995, 11, 26));
		Assert.assertTrue(officials.size() == 0);
	}

	@Test
	public void getGameOfficials_found() {
		when(officialJpaService.findByLastNameAndFirstNameAndAsOfDate(anyString(), anyString(), anyObject()))
			.thenReturn(createMockOfficial("Adams", "Samuel", StatusCodeDAO.Found))
			.thenReturn(createMockOfficial("Coors", "Adolph", StatusCodeDAO.Found));
		List<GameOfficial> officials = officialAppService.getGameOfficials(createMockOfficialDTOs(), createMockGame(), new LocalDate(1995, 11, 26));
		Assert.assertEquals(2, officials.size());
		Assert.assertEquals("Coors", officials.get(1).getOfficial().getLastName());
		Assert.assertEquals("Adolph", officials.get(1).getOfficial().getFirstName());
	}

	private OfficialDTO[] createMockOfficialDTOs() {
		OfficialDTO[] officials = new OfficialDTO[2];
		officials[0] = createMockOfficialDTO("Adams", "Samuel");
		officials[1] = createMockOfficialDTO("Coors", "Adolph");
		return officials;
	}

	private OfficialDTO createMockOfficialDTO(String lastName, String firstName) {
		OfficialDTO official = new OfficialDTO();
		official.setLast_name(lastName);
		official.setFirst_name(firstName);
		return official;
	}

	private Official createMockOfficial(String lastName, String firstName, StatusCodeDAO statusCode) {
		Official official = new Official();
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setStatusCode(statusCode);
		return official;
	}

	private Game createMockGame() {
		return new Game();
	}
}