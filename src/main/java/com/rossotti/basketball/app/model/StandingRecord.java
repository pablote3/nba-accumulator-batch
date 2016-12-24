package com.rossotti.basketball.app.model;

public class StandingRecord {
	private Integer gamesWon;
	private Integer gamesPlayed;
	private Integer opptGamesWon;
	private Integer opptGamesPlayed;

	public StandingRecord(Integer gamesWon, Integer gamesPlayed, Integer opptGamesWon, Integer opptGamesPlayed) {
		this.gamesWon = gamesWon;
		this.gamesPlayed = gamesPlayed;
		this.opptGamesWon = opptGamesWon;
		this.opptGamesPlayed = opptGamesPlayed;
	}

	public Integer getGamesWon() {
		return gamesWon;
	}
	public void setGamesWon(Integer gamesWon) {
		this.gamesWon = gamesWon;
	}

	public Integer getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(Integer gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}	

	public Integer getOpptGamesWon() {
		return opptGamesWon;
	}
	public void setOpptGamesWon(Integer opptGamesWon) {
		this.opptGamesWon = opptGamesWon;
	}

	public Integer getOpptGamesPlayed() {
		return opptGamesPlayed;
	}
	public void setOpptGamesPlayed(Integer opptGamesPlayed) {
		this.opptGamesPlayed = opptGamesPlayed;
	}
}
