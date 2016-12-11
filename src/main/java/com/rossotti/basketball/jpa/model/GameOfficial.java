package com.rossotti.basketball.jpa.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="gameOfficial")
public class GameOfficial extends AbstractDomainClass {

	public GameOfficial() {}

	@ManyToOne
	@JoinColumn(name="gameId", referencedColumnName="id", nullable=false)
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne
	@JoinColumn(name="officialId", referencedColumnName="id", nullable=false)
	private Official official;
	public Official getOfficial() {
		return official;
	}
	public void setOfficial(Official official) {
		this.official = official;
	}

	public String toString() {
		return ("  id: " + this.id + "\n") +
				"  official: " + this.official.toString() + "\n";
	}
}