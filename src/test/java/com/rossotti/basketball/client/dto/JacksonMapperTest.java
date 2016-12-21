package com.rossotti.basketball.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileClientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JacksonMapperTest {

	private FileClientService fileClientService;

	@Autowired
	public void setFileClientService(FileClientService fileClientService) {
		this.fileClientService = fileClientService;
	}

	ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	@Test
	public void deserializeRoster() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/rosterClient.json");
		RosterDTO roster = objectMapper.readValue(baseJson, RosterDTO.class);
		Assert.assertEquals("detroit-pistons", roster.team.getTeam_id());
		Assert.assertEquals("Eskişehir, Turkey", roster.players[8].getBirthplace());
		Assert.assertEquals("Ersan Ilyasova", roster.players[8].getDisplay_name());
		Assert.assertEquals(LocalDate.of(1987, 5, 15), roster.players[8].getBirthdate());
		baseJson.close();
	}

	@Test
	public void deserializeGame() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json");
		GameDTO game = objectMapper.readValue(baseJson, GameDTO.class);
		Assert.assertEquals("detroit-pistons", game.away_team.getTeam_id());
		Assert.assertEquals(17, game.home_period_scores[1]);
		Assert.assertEquals("Bojan Bogdanović", game.home_stats[0].getDisplay_name());
		Assert.assertEquals(0f, game.home_stats[0].getFree_throw_percentage(), 0.0f);
		Assert.assertEquals("Zarba", game.officials[0].getLast_name());
		Assert.assertEquals("completed", game.event_information.getStatus());
		Assert.assertEquals(LocalDateTime.of(2015, 11, 29, 18, 0), LocalDateTime.ofInstant(game.event_information.getStart_date_time().toInstant(), ZoneId.of("US/Eastern")));
		Assert.assertTrue(game.away_totals.getThree_point_field_goals_attempted().equals((short)24));
		baseJson.close();
	}

	@Test
	public void deserializeStandings() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/standingsClient.json");
		StandingsDTO standings = objectMapper.readValue(baseJson, StandingsDTO.class);
		Assert.assertEquals(LocalDateTime.of(2016, 2, 11, 22, 19), LocalDateTime.ofInstant(standings.standings_date.toInstant(), ZoneId.of("US/Eastern")));
		Assert.assertEquals(30, standings.standing.length);
		Assert.assertEquals("W3", standings.standing[0].getStreak());
		Assert.assertEquals("toronto-raptors", standings.standing[1].getTeam_id());
		baseJson.close();
	}
}