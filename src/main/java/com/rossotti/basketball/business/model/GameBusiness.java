package com.rossotti.basketball.business.model;

import com.rossotti.basketball.jpa.model.Game;

public class GameBusiness extends StatusCodeBusiness {
	public GameBusiness(Game game) {
		this.game = game;
	}

	public GameBusiness(Game game, StatusCode statusCode) {
		this.game = game;
		this.setStatusCode(statusCode);
	}

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
