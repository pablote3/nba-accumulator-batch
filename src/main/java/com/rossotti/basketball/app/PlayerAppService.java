package com.rossotti.basketball.app;

import com.rossotti.basketball.jpa.model.Player;
import com.rossotti.basketball.jpa.service.PlayerJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PlayerAppService {
	private final PlayerJpaService playerJpaService;

	@Autowired
	public PlayerAppService(PlayerJpaService playerJpaService) {
		this.playerJpaService = playerJpaService;
	}

	public Player findByPlayerNameBirthdate(String lastName, String firstName, LocalDate birthdate) {
		return playerJpaService.findByLastNameAndFirstNameAndBirthdate(lastName, firstName, birthdate);
	}

	public Player createPlayer(Player player) {
		return playerJpaService.create(player);
	}
}