package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Player;
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
public class PlayerServiceTest {

	private PlayerService playerService;

	@Autowired
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Test
	public void getById() {
		Player player = playerService.getById(1L);
		Assert.assertEquals("Puzdrakiew'icz", player.getLastName());
		Assert.assertEquals(2, player.getRosterPlayers().size());
	}

	@Test
	public void listAll() {
		List<Player> players = (List<Player>)playerService.listAll();
		Assert.assertTrue(players.size() >= 14);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_Found() {
		Player player = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals("Sacramento, CA, USA", player.getBirthplace());
		Assert.assertTrue(player.isFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_LastName() {
		Player player = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew''icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertTrue(player.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_FirstName() {
		Player player = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Like", LocalDate.of(2002, 2, 20));
		Assert.assertTrue(player.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_Birthdate() {
		Player player = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21));
		Assert.assertTrue(player.isNotFound());
	}

	@Test
	public void findPlayerByName_Found_UTF_8() {
		Player findPlayer = playerService.findByLastNameAndFirstNameAndBirthdate("Valančiūnas", "Jonas", LocalDate.of(1992, 5, 6));
		Assert.assertEquals("Jonas Valančiūnas", findPlayer.getDisplayName());
		Assert.assertEquals("Utėnai, Lithuania", findPlayer.getBirthplace());
		Assert.assertTrue(findPlayer.isFound());
	}

	@Test
	public void findByAsLastNameFirstName_Found() {
		List<Player> players = playerService.findByLastNameAndFirstName("Puzdrakiewicz", "Thad");
		Assert.assertEquals(2, players.size());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		List<Player> players = playerService.findByLastNameAndFirstName("Puzdrakiewiczy", "Thad");
		Assert.assertEquals(0, players.size());
	}

	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
		List<Player> players = playerService.findByLastNameAndFirstName("Puzdrakiewicz", "Thady");
		Assert.assertEquals(0, players.size());
	}

	@Test
	public void create_Created() {
		Player createPlayer = playerService.create(createMockPlayer("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 9), "Fred Puzdrakiewicz"));
		Player findPlayer = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 9));
		Assert.assertTrue(createPlayer.isCreated());
		Assert.assertEquals("Fred Puzdrakiewicz", findPlayer.getDisplayName());
	}

	@Test
	public void create_Existing() {
		Player createPlayer = playerService.create(createMockPlayer("Puzdrakiewicz", "Michelle", LocalDate.of(1969, 9, 8), "Michelle Puzdrakiewicz"));
		Assert.assertTrue(createPlayer.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		Player createPlayer = playerService.create(createMockPlayer("Puzdrakiewicz", "Fred", LocalDate.of(1969, 11, 9), null));
	}

	@Test
	public void update_Updated() {
		Player updatePlayer = playerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13), "Thad Puzdrakiewicz2"));
		Player player = playerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13));
		Assert.assertEquals("Thad Puzdrakiewicz2", player.getDisplayName());
		Assert.assertTrue(updatePlayer.isUpdated());
	}

	@Test
	public void update_NotFound() {
		Player player = playerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 14), "Thad Puzdrakiewicz"));
		Assert.assertTrue(player.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		Player player = playerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13), null));
	}

	@Test
	public void delete_Deleted() {
		Player deletePlayer = playerService.delete(6L);
		Player findPlayer = playerService.getById(6L);
		Assert.assertNull(findPlayer);
		Assert.assertTrue(deletePlayer.isDeleted());
	}

	@Test
	public void delete_NotFound() {
		Player deletePlayer = playerService.delete(101L);
		Assert.assertTrue(deletePlayer.isNotFound());
	}

	private Player createMockPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		player.setDisplayName(displayName);
		player.setHeight((short)79);
		player.setWeight((short)195);
		player.setBirthplace("Monroe, Louisiana, USA");
		return player;
	}
}
