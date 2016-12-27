package com.rossotti.basketball.business.model;

import com.rossotti.basketball.jpa.model.Game;

public class GameBusiness extends StatusCodeBusiness {

	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	private String rosterLastTeam;
	public String getRosterLastTeam() {
		return rosterLastTeam;
	}
	public void setRosterLastTeam(String rosterLastTeam) {
		this.rosterLastTeam = rosterLastTeam;
	}
}
