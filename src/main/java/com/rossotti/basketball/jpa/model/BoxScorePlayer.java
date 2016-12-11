package com.rossotti.basketball.jpa.model;

import com.rossotti.basketball.jpa.model.RosterPlayer.Position;

import javax.persistence.*;

@Entity
@Table(name="boxScorePlayer")
public class BoxScorePlayer extends AbstractDomainClass {

	public BoxScorePlayer() {}

	@ManyToOne()
	@JoinColumn(name="boxScoreId", referencedColumnName="id", nullable=false)
	private BoxScore boxScore;
	public BoxScore getBoxScore() {
		return boxScore;
	}
	public void setBoxScore(BoxScore boxScore) {
		this.boxScore = boxScore;
	}

	@ManyToOne()
	@JoinColumn(name="rosterPlayerId", referencedColumnName="id", nullable=false)
	private RosterPlayer rosterPlayer;
	public RosterPlayer getRosterPlayer() {
		return rosterPlayer;
	}
	public void setRosterPlayer(RosterPlayer rosterPlayer) {
		this.rosterPlayer = rosterPlayer;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="position", length=5, nullable=false)
	private Position position;
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}

	@Column(name="starter")
	private boolean starter;
	public boolean getStarter() {
		return starter;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	@Embedded
	private BoxScoreStats boxScoreStats;
	public BoxScoreStats getBoxScoreStats() {
		return boxScoreStats;
	}
	public void setBoxScoreStats(BoxScoreStats boxScoreStats) {
		this.boxScoreStats = boxScoreStats;
	}

	public String toString() {
		return ("  id: " + this.id + "\n") +
				"  position: " + this.position + "\n" +
				"  points: " + this.getBoxScoreStats().getPoints() + "\n" +
				"  assists: " + this.getBoxScoreStats().getAssists() + "\n" +
				"  turnovers: " + this.getBoxScoreStats().getTurnovers() + "\n" +
				"  steals: " + this.getBoxScoreStats().getSteals() + "\n" +
				"  blocks: " + this.getBoxScoreStats().getBlocks();
	}
}