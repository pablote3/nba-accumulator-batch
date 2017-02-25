package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Standing;
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
public class StandingRepositoryTest {

	private StandingRepository standingRepository;

	@Autowired
	public void setStandingRepository(StandingRepository standingRepository) {
		this.standingRepository = standingRepository;
	}

	@Test
	public void getById() {
		Standing standing = standingRepository.findOne(1L);
		Assert.assertEquals("1st", standing.getOrdinalRank());
		Assert.assertEquals("Chicago Zephyr\'s", standing.getTeam().getFullName());
	}

	@Test
	public void findAll() {
		List<Standing> standings = standingRepository.findAll();
		Assert.assertEquals(22, standings.size());
	}

	@Test
	public void findByTeamKeyAsOfDate_Found() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr's", LocalDate.of(2015, 10, 30));
		Assert.assertEquals("1st", standing.getOrdinalRank());
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_TeamKey() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr", LocalDate.of(2015, 10, 30));
		Assert.assertNull(standing);
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_AsOfDate() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr's", LocalDate.of(2015, 10, 29));
		Assert.assertNull(standing);
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Standing> standings = standingRepository.findByStandingDate(LocalDate.of(2015, 10, 30));
		Assert.assertEquals(2, standings.size());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Standing> standings = standingRepository.findByStandingDate(LocalDate.of(2015, 10, 29));
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void findByTeamKey_Found() {
		List<Standing> standings = standingRepository.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(7, standings.size());
	}

	@Test
	public void findByTeamKey_NotFound() {
		List<Standing> standings = standingRepository.findByTeamKey("st-louis-bomber");
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void create_Created() {
		standingRepository.save(createMockStanding(20L, LocalDate.of(2012, 7, 1), "10th"));
		Standing findStanding = standingRepository.findByTeamKeyAndStandingDate("chicago-bulls", LocalDate.of(2012, 7, 1));
		Assert.assertEquals("10th", findStanding.getOrdinalRank());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		standingRepository.save(createMockStanding(1L, LocalDate.of(2015, 10, 30), "10th"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		standingRepository.save(createMockStanding(20L, LocalDate.of(2012, 7, 1), null));
	}

	@Test
	public void update_Updated() {
		standingRepository.save(updateMockStanding(4L, 4L, LocalDate.of(2015, 10, 31), "10th"));
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("salinas-cowboys", LocalDate.of(2015, 10, 31));
		Assert.assertEquals("10th", standing.getOrdinalRank());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		standingRepository.save(updateMockStanding(4L, 4L, LocalDate.of(2015, 10, 31), null));
	}

	@Test
	public void delete_Deleted() {
		standingRepository.delete(5L);
		Standing standing = standingRepository.findOne(5L);
		Assert.assertNull(standing);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void delete_NotFound() {
		standingRepository.delete(101L);
	}

	private Standing createMockStanding(Long teamId, LocalDate asOfDate, String ordinalRank) {
		Standing standing = new Standing();
		standing.setTeam(getMockTeam(teamId));
		standing.setStandingDate(asOfDate);
		standing.setRank((short)3);
		standing.setOrdinalRank(ordinalRank);
		standing.setGamesWon((short)15);
		standing.setGamesLost((short)25);
		standing.setStreak("L5");
		standing.setStreakType("loss");
		standing.setStreakTotal((short)5);
		standing.setGamesBack((float)3.5);
		standing.setPointsFor((short)1895);
		standing.setPointsAgainst((short)2116);
		standing.setHomeWins((short)10);
		standing.setHomeLosses((short)10);
		standing.setAwayWins((short)5);
		standing.setAwayLosses((short)15);
		standing.setConferenceWins((short)7);
		standing.setConferenceLosses((short)8);
		standing.setLastFive("0-5");
		standing.setLastTen("3-7");
		standing.setGamesPlayed((short)40);
		standing.setPointsScoredPerGame((float)95.5);
		standing.setPointsAllowedPerGame((float)102.5);
		standing.setWinPercentage((float)0.375);
		standing.setPointDifferential((short)221);
		standing.setPointDifferentialPerGame((float)7.0);
		standing.setOpptGamesWon(4);
		standing.setOpptGamesPlayed(5);
		standing.setOpptOpptGamesWon(15);
		standing.setOpptOpptGamesPlayed(20);
		return standing;
	}

	private Team getMockTeam(Long id) {
		Team team = new Team();
		team.setId(id);
		return team;
	}

	private Standing updateMockStanding(Long id, Long teamId, LocalDate asOfDate, String ordinalRank) {
		Standing standing = createMockStanding(teamId, asOfDate, ordinalRank);
		standing.setId(id);
		return standing;
	}
}