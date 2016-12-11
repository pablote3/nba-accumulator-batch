package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Official;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficialServiceTest {

	private OfficialService officialService;

	@Autowired
	public void setOfficialService(OfficialService officialService) {
		this.officialService = officialService;
	}

	@Test
	public void getById() {
		Official official = officialService.getById(1L);
		Assert.assertEquals("LateCa'll", official.getLastName());
	}

	@Test
	public void listAll() {
		List<Official> officials = (List<Official>)officialService.listAll();
		Assert.assertTrue(officials.size() >= 11);
	}

	@Test
	public void findByLastNameFirstName_Found_FromDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("96", official.getNumber());
		Assert.assertTrue(official.isFound());
	}

	@Test
	public void findByLastNameFirstName_Found_ToDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("96", official.getNumber());
		Assert.assertTrue(official.isFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCall", "Joe", LocalDate.of(2010, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joey", LocalDate.of(2010, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_BeforeAsOfDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_AfterAsOfDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2010, 7, 1));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Official> officials = officialService.findByAsOfDate(LocalDate.of(2009, 10, 30));
		Assert.assertTrue(officials.size() >= 3);
	}

	@Test
	public void findByDateRange_NotFound() {
		List<Official> officials = officialService.findByAsOfDate(LocalDate.of(1989, 10, 30));
		Assert.assertEquals(0, officials.size());
	}

	@Test
	public void create_Created_AsOfDate() {
		Official createOfficial = officialService.create(createMockOfficial("BadCall", "Melvin", LocalDate.of(2006, 7, 6), LocalDate.of(9999, 12, 31), "996"));
		Official findOfficial = officialService.findByLastNameAndFirstNameAndAsOfDate("BadCall", "Melvin", LocalDate.of(2006, 7, 6));
		Assert.assertTrue(createOfficial.isCreated());
		Assert.assertEquals("996", findOfficial.getNumber());
	}

	@Test
	public void create_Created_DateRange() {
		Official createOfficial = officialService.create(createMockOfficial("BadCall", "Melvon", LocalDate.of(2006, 7, 6), LocalDate.of(2006, 7, 10), "995"));
		Official findOfficial = officialService.findByLastNameAndFirstNameAndAsOfDate("BadCall", "Melvon", LocalDate.of(2006, 7, 7));
		Assert.assertTrue(createOfficial.isCreated());
		Assert.assertEquals("995", findOfficial.getNumber());
	}

	@Test
	public void create_OverlappingDates() {
		Official createOfficial = officialService.create(createMockOfficial("QuestionableCall", "Hefe", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 20), "18"));
		Assert.assertTrue(createOfficial.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		officialService.create(createMockOfficial("BadCaller", "Melvyn", LocalDate.of(2006, 7, 6), LocalDate.of(2006, 7, 10), null));
	}

	@Test
	public void update_Updated() {
		Official updateOfficial = officialService.update(createMockOfficial("Forte", "Brian", LocalDate.of(2010, 4, 25), LocalDate.of(2012, 12, 31), "19"));
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("Forte", "Brian", LocalDate.of(2010, 4, 25));
		Assert.assertEquals("19", official.getNumber());
		Assert.assertEquals(LocalDate.of(2012, 12, 31), official.getToDate());
		Assert.assertTrue(updateOfficial.isUpdated());
	}

	@Test
	public void update_NotFound() {
		Official updateOfficial = officialService.update(createMockOfficial("Forte", "Brian", LocalDate.of(2009, 4, 25), LocalDate.of(2009, 12, 31), "19"));
		Assert.assertTrue(updateOfficial.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		officialService.update(createMockOfficial("Forte", "Brian", LocalDate.of(2010, 4, 25), LocalDate.of(2012, 12, 31), null));
	}

	@Test
	public void delete_Deleted() {
		Official deleteOfficial = officialService.delete(21L);
		Official findOfficial = officialService.getById(21L);
		Assert.assertNull(findOfficial);
		Assert.assertTrue(deleteOfficial.isDeleted());
	}

	@Test
	public void delete_NotFound() {
		Official deleteOfficial = officialService.delete(101L);
		Assert.assertTrue(deleteOfficial.isNotFound());
	}

	private Official createMockOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate, String number) {
		Official official = new Official();
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setFromDate(fromDate);
		official.setToDate(toDate);
		official.setNumber(number);
		return official;
	}
}
