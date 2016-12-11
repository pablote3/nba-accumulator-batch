package com.rossotti.basketball.jpa.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="rosterPlayer", uniqueConstraints=@UniqueConstraint(columnNames={"playerId", "teamId", "fromDate", "toDate"}))
public class RosterPlayer extends AbstractDomainClass {

	public RosterPlayer() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public RosterPlayer(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@ManyToOne
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@ManyToOne
	@JoinColumn(name="playerId", referencedColumnName="id", nullable=false)
	private Player player;
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	@OneToMany(mappedBy="rosterPlayer", fetch = FetchType.LAZY)
	private List<BoxScorePlayer> boxScorePlayers = new ArrayList<>();
	private List<BoxScorePlayer> getBoxScorePlayers()  {
		return boxScorePlayers;
	}
	public void setBoxScorePlayers(List<BoxScorePlayer> boxScorePlayers)  {
		this.boxScorePlayers = boxScorePlayers;
	}
	public void addBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().add(boxScorePlayer);
	}
	public void removeBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().remove(boxScorePlayer);
	}

	@Column(name="fromDate", nullable=false)
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	private LocalDate toDate;
	public LocalDate getToDate()  {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="number", length=3, nullable=false)
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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

	public enum Position {
		PG,
		SG,
		SF,
		PF,
		C,
		G,
		F
	}

	public String toString() {
		return ("\r" + "  id: " + this.id + "\n") +
				"  lastName: " + player.getLastName() + "\n" +
				"  firstName: " + player.getFirstName() + "\n" +
				"  birthDate: " + player.getBirthdate() + "\n" +
				"  teamKey: " + team.getTeamKey() + "\n" +
				"  fromDate: " + this.getFromDate() + "\n" +
				"  toDate: " + this.getToDate() + "\n";
	}
}