package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Official;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import org.joda.time.LocalDate;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficialRepositoryTest {

	private OfficialRepository officialRepository;

	@Autowired
	public void setOfficialRepository(OfficialRepository officialRepository) {
		this.officialRepository = officialRepository;
	}

	@Test
	public void getById() {
		Official official = officialRepository.findOne(1L);
		Assert.assertEquals("LateCa'll", official.getLastName());
	}

	@Test
	public void findAll() {
		List<Official> officials = officialRepository.findAll();
		Assert.assertTrue(officials.size() >= 11);
	}

	@Test
	public void findByLastNameFirstName_Found_FromDate() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", new LocalDate(2009, 7, 1), new LocalDate(2009, 7, 1));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findByLastNameFirstName_Found_ToDate() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", new LocalDate(2010, 6, 30), new LocalDate(2010, 6, 30));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCall", "Joe", new LocalDate(2010, 6, 30), new LocalDate(2010, 6, 30));
		Assert.assertNull(official);
	}

 	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
	    Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joey", new LocalDate(2010, 6, 30), new LocalDate(2010, 6, 30));
		Assert.assertNull(official);
	}

	@Test
	public void findByLastNameFirstName_NotFound_BeforeAsOfDate() {
		Official team = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", new LocalDate(2009, 6, 30), new LocalDate(2009, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByLastNameFirstName_NotFound_AfterAsOfDate() {
		Official team = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", new LocalDate(2010, 6, 30), new LocalDate(2010, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Official> officials = officialRepository.findByFromDateAndToDate(new LocalDate(2009, 7, 1), new LocalDate(2009, 7, 1));
		Assert.assertTrue(officials.size() >= 3);
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Official> officials = officialRepository.findByFromDateAndToDate(new LocalDate(1989, 7, 1), new LocalDate(1989, 7, 1));
		Assert.assertEquals(0, officials.size());
	}

	@Test
	public void create_Created() {
		officialRepository.save(createMockOfficial("BadCall", "Melvin", new LocalDate(2006, 7, 1), new LocalDate(2006, 7, 5), "999"));
		Official findOfficial = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("BadCall", "Melvin", new LocalDate(2006, 7, 1), new LocalDate(2006, 7, 5));
		Assert.assertEquals("999", findOfficial.getNumber());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		officialRepository.save(createMockOfficial("TerribleCall", "Limo", new LocalDate(2005, 7, 1), new LocalDate(2006, 6, 30), "100"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		officialRepository.save(createMockOfficial("BadCall", "Melvin", new LocalDate(2006, 7, 1), new LocalDate(9999, 12, 31), null));
	}

	@Test
	public void update_Updated() {
		officialRepository.save(updateMockOfficial(10L, "Zarba", "Zach", new LocalDate(2010, 10, 30), new LocalDate(9999, 12, 31), "58"));
		Official team = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("Zarba", "Zach", new LocalDate(2010, 10, 30), new LocalDate(9999, 12, 31));
		Assert.assertEquals("58", team.getNumber());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		officialRepository.save(updateMockOfficial(10L, "Zarba", "Zach", new LocalDate(2010, 10, 30), new LocalDate(9999, 12, 31), null));
	}

	@Test
	public void delete_Deleted() {
		officialRepository.delete(20L);
		Official findOfficial = officialRepository.findOne(20L);
		Assert.assertNull(findOfficial);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void delete_NotFound() {
		officialRepository.delete(101L);
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

	private Official updateMockOfficial(Long id, String lastName, String firstName, LocalDate fromDate, LocalDate toDate, String number) {
		Official official = createMockOfficial(lastName, firstName, fromDate, toDate, number);
		official.setId(id);
		return official;
	}
}