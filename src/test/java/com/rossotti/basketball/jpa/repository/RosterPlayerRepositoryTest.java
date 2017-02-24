package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Player;
import com.rossotti.basketball.jpa.model.RosterPlayer;
import com.rossotti.basketball.jpa.model.Team;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterPlayerRepositoryTest {

	private RosterPlayerRepository rosterPlayerRepository;

	@Autowired
	public void setRosterPlayerRepository(RosterPlayerRepository rosterPlayerRepository) {
		this.rosterPlayerRepository = rosterPlayerRepository;
	}

	@Test
	public void getById() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findOne(1L);
		Assert.assertEquals("21", rosterPlayer.getNumber());
		Assert.assertEquals("chicago-zephyr's", rosterPlayer.getTeam().getTeamKey());
		Assert.assertEquals("Luke", rosterPlayer.getPlayer().getFirstName());
	}

	@Test
	public void findAll() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findAll();
		Assert.assertEquals(20, rosterPlayers.size());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_Found() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertEquals("31", rosterPlayer.getNumber());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_Found_UTF_8() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Valančiūnas", "Jonas", LocalDate.of(1992, 5, 6), LocalDate.of(2015, 10, 30));
		Assert.assertEquals("9", rosterPlayer.getNumber());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_LastName() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'iczy", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_FirstName() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Lukey", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_Birthdate() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21), LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_AsOfDate() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 29));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameTeamKey_Found() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertEquals("31", rosterPlayer.getNumber());
	}

	@Test
	public void findByLastNameFirstNameTeamKey_Found_UTF_8() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Valančiūnas", "Jonas", "detroit-pistons", LocalDate.of(2015, 10, 30));
		Assert.assertEquals("9", rosterPlayer.getNumber());
	}

	@Test
	public void findByLastNameFirstNameTeamKey_NotFound_LastName() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'iczy", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameTeamKey_NotFound_FirstName() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Lukey", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameTeamKey_NotFound_TeamKey() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboy", LocalDate.of(2009, 10, 30));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameFirstNameTeamKey_NotFound_AsOfDate() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 29));
		Assert.assertNull(rosterPlayer);
	}

	@Test
	public void findByLastNameAndFirstNameAndBirthdate_Found() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(3, rosterPlayers.size());
	}

	@Test
	public void findByLastNameAndFirstNameAndBirthdate_Found_UTF_8() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Valančiūnas", "Jonas", LocalDate.of(1992, 5, 6));
		Assert.assertEquals(1, rosterPlayers.size());
	}

	@Test
	public void findByLastNameAndFirstNameAndBirthdate_NotFound_LastName() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'iczy", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByLastNameAndFirstNameAndBirthdate_NotFound_FirstName() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Lukey", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByLastNameAndFirstNameAndBirthdate_NotFound_Birthdate() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_Found() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByTeamKeyAndAsOfDate("detroit-pistons", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(4, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_TeamKey() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByTeamKeyAndAsOfDate("detroit-pistols", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_AsOfDate() {
		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByTeamKeyAndAsOfDate("detroit-pistons", LocalDate.of(2014, 10, 30));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void create_Created() {
		rosterPlayerRepository.save(createMockRosterPlayer(2L, 2L, LocalDate.of(2010, 1, 11), LocalDate.of(2010, 1, 21), "22"));
		RosterPlayer findRosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2010, 1, 21));
		Assert.assertEquals("22", findRosterPlayer.getNumber());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		rosterPlayerRepository.save(createMockRosterPlayer(2L, 2L, LocalDate.of(2010, 1, 1), LocalDate.of(2010, 1, 10), "44"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		rosterPlayerRepository.save(createMockRosterPlayer(2L, 2L, LocalDate.of(2010, 1, 1), LocalDate.of(2010, 1, 10), null));
	}

	@Test
	public void update_Updated() {
		rosterPlayerRepository.save(updateMockRosterPlayer(10L, 10L, 9L, LocalDate.of(2015, 11, 15), LocalDate.of(9999, 12, 31),"51"));
		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Drummond", "Andre", LocalDate.of(1990, 3, 4), LocalDate.of(2015, 11, 15));
		Assert.assertEquals("51", rosterPlayer.getNumber());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		rosterPlayerRepository.save(updateMockRosterPlayer(10L, 10L, 9L, LocalDate.of(2015, 11, 15), LocalDate.of(9999, 12, 31),null));
	}

	@Test
	public void delete_Deleted() {
		rosterPlayerRepository.delete(22L);
		RosterPlayer findRosterPlayer = rosterPlayerRepository.findOne(22L);
		Assert.assertNull(findRosterPlayer);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void delete_NotFound() {
		rosterPlayerRepository.delete(101L);
	}

	private RosterPlayer createMockRosterPlayer(Long playerId, Long teamId, LocalDate fromDate, LocalDate toDate, String number) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(getMockPlayer(playerId));
		rosterPlayer.setTeam(getMockTeam(teamId));
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setNumber(number);
		rosterPlayer.setPosition(RosterPlayer.Position.G);
		return rosterPlayer;
	}

	private RosterPlayer updateMockRosterPlayer(Long id, Long playerId, Long teamId, LocalDate fromDate, LocalDate toDate, String number) {
		RosterPlayer rosterPlayer = createMockRosterPlayer(playerId, teamId, fromDate, toDate, number);
		rosterPlayer.setId(id);
		return rosterPlayer;
	}

	private Player getMockPlayer(Long playerId) {
		Player player = new Player();
		player.setId(playerId);
		return player;
	}

	private Team getMockTeam(Long teamId) {
		Team team = new Team();
		team.setId(teamId);
		return team;
	}
}