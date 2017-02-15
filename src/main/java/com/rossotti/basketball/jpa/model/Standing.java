package com.rossotti.basketball.jpa.model;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@Entity
@Table(name="standing", uniqueConstraints=@UniqueConstraint(columnNames={"teamId", "standingDate"}))
public class Standing extends AbstractDomainClass {

	public Standing() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public Standing(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@ManyToOne()
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@Column(name="standingDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate standingDate;
	public LocalDate getStandingDate() {
		return standingDate;
	}
	public void setStandingDate(LocalDate standingDate) {
		this.standingDate = standingDate;
	}

	@Column(name="rank", nullable=false)
	private Short rank;
	public Short getRank() {
		return rank;
	}
	public void setRank(Short rank) {
		this.rank = rank;
	}

	@Column(name="ordinalRank", length=4, nullable=false)
	private String ordinalRank;
	public String getOrdinalRank() {
		return ordinalRank;
	}
	public void setOrdinalRank(String ordinalRank) {
		this.ordinalRank = ordinalRank;
	}

	@Column(name="gamesWon", nullable=false)
	private Short gamesWon;
	public Short getGamesWon() {
		return gamesWon;
	}
	public void setGamesWon(Short gamesWon) {
		this.gamesWon = gamesWon;
	}

	@Column(name="gamesLost", nullable=false)
	private Short gamesLost;
	public Short getGamesLost() {
		return gamesLost;
	}
	public void setGamesLost(Short gamesLost) {
		this.gamesLost = gamesLost;
	}

	@Column(name="streak", length=4, nullable=false)
	private String streak;
	public String getStreak() {
		return streak;
	}
	public void setStreak(String streak) {
		this.streak = streak;
	}

	@Column(name="streakType", length=4, nullable=false)
	private String streakType;
	public String getStreakType() {
		return streakType;
	}
	public void setStreakType(String streakType) {
		this.streakType = streakType;
	}

	@Column(name="streakTotal", nullable=false)
	private Short streakTotal;
	public Short getStreakTotal() {
		return streakTotal;
	}
	public void setStreakTotal(Short streakTotal) {
		this.streakTotal = streakTotal;
	}

	@Column(name="gamesBack", nullable=false)
	private Float gamesBack;
	public Float getGamesBack() {
		return gamesBack;
	}
	public void setGamesBack(Float gamesBack) {
		this.gamesBack = gamesBack;
	}

	@Column(name="pointsFor", nullable=false)
	private Short pointsFor;
	public Short getPointsFor() {
		return pointsFor;
	}
	public void setPointsFor(Short pointsFor) {
		this.pointsFor = pointsFor;
	}
	
	@Column(name="pointsAgainst", nullable=false)
	private Short pointsAgainst;
	public Short getPointsAgainst() {
		return pointsAgainst;
	}
	public void setPointsAgainst(Short pointsAgainst) {
		this.pointsAgainst = pointsAgainst;
	}

	@Column(name="homeWins", nullable=false)
	private Short homeWins;
	public Short getHomeWins() {
		return homeWins;
	}
	public void setHomeWins(Short homeWins) {
		this.homeWins = homeWins;
	}

	@Column(name="homeLosses", nullable=false)
	private Short homeLosses;
	public Short getHomeLosses() {
		return homeLosses;
	}
	public void setHomeLosses(Short homeLosses) {
		this.homeLosses = homeLosses;
	}

	@Column(name="awayWins", nullable=false)
	private Short awayWins;
	public Short getAwayWins() {
		return awayWins;
	}
	public void setAwayWins(Short awayWins) {
		this.awayWins = awayWins;
	}

	@Column(name="awayLosses", nullable=false)
	private Short awayLosses;
	public Short getAwayLosses() {
		return awayLosses;
	}
	public void setAwayLosses(Short awayLosses) {
		this.awayLosses = awayLosses;
	}

	@Column(name="conferenceWins", nullable=false)
	private Short conferenceWins;
	public Short getConferenceWins() {
		return conferenceWins;
	}
	public void setConferenceWins(Short conferenceWins) {
		this.conferenceWins = conferenceWins;
	}

	@Column(name="conferenceLosses", nullable=false)
	private Short conferenceLosses;
	public Short getConferenceLosses() {
		return conferenceLosses;
	}
	public void setConferenceLosses(Short conferenceLosses) {
		this.conferenceLosses = conferenceLosses;
	}

	@Column(name="lastFive", length=4, nullable=false)
	private String lastFive;
	public String getLastFive() {
		return lastFive;
	}
	public void setLastFive(String lastFive) {
		this.lastFive = lastFive;
	}

	@Column(name="lastTen", length=4, nullable=false)
	private String lastTen;
	public String getLastTen() {
		return lastTen;
	}
	public void setLastTen(String lastTen) {
		this.lastTen = lastTen;
	}

	@Column(name="gamesPlayed", nullable=false)
	private Short gamesPlayed;
	public Short getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(Short gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	@Column(name="pointsScoredPerGame", nullable=false)
	private Float pointsScoredPerGame;
	public Float getPointsScoredPerGame() {
		return pointsScoredPerGame;
	}
	public void setPointsScoredPerGame(Float pointsScoredPerGame) {
		this.pointsScoredPerGame = pointsScoredPerGame;
	}

	@Column(name="pointsAllowedPerGame", nullable=false)
	private Float pointsAllowedPerGame;
	public Float getPointsAllowedPerGame() {
		return pointsAllowedPerGame;
	}
	public void setPointsAllowedPerGame(Float pointsAllowedPerGame) {
		this.pointsAllowedPerGame = pointsAllowedPerGame;
	}

	@Column(name="winPercentage", nullable=false)
	private Float winPercentage;
	public Float getWinPercentage() {
		return winPercentage;
	}
	public void setWinPercentage(Float winPercentage) {
		this.winPercentage = winPercentage;
	}

	@Column(name="pointDifferential", nullable=false)
	private Short pointDifferential;
	public Short getPointDifferential() {
		return pointDifferential;
	}
	public void setPointDifferential(Short pointDifferential) {
		this.pointDifferential = pointDifferential;
	}

	@Column(name="pointDifferentialPerGame", nullable=false)
	private Float pointDifferentialPerGame;
	public Float getPointDifferentialPerGame() {
		return pointDifferentialPerGame;
	}
	public void setPointDifferentialPerGame(Float pointDifferentialPerGame) {
		this.pointDifferentialPerGame = pointDifferentialPerGame;
	}

	@Column(name="opptGamesWon")
	private Integer opptGamesWon;
	public Integer getOpptGamesWon() {
		return opptGamesWon;
	}
	public void setOpptGamesWon(Integer opptGamesWon) {
		this.opptGamesWon = opptGamesWon;
	}

	@Column(name="opptGamesPlayed")
	private Integer opptGamesPlayed;
	public Integer getOpptGamesPlayed() {
		return opptGamesPlayed;
	}
	public void setOpptGamesPlayed(Integer opptGamesPlayed) {
		this.opptGamesPlayed = opptGamesPlayed;
	}

	@Column(name="opptOpptGamesWon")
	private Integer opptOpptGamesWon;
	public Integer getOpptOpptGamesWon() {
		return opptOpptGamesWon;
	}
	public void setOpptOpptGamesWon(Integer opptOpptGamesWon) {
		this.opptOpptGamesWon = opptOpptGamesWon;
	}

	@Column(name="opptOpptGamesPlayed")
	private Integer opptOpptGamesPlayed;
	public Integer getOpptOpptGamesPlayed() {
		return opptOpptGamesPlayed;
	}	
	public void setOpptOpptGamesPlayed(Integer opptOpptGamesPlayed) {
		this.opptOpptGamesPlayed = opptOpptGamesPlayed;
	}

	public String toString() {
		return ("\r" + "  id: " + this.id + "\n") +
				"  standingDate: " + this.standingDate + "\n" +
				"  teamKey: " + team.getTeamKey() + "\n" +
				"  gamesWon: " + this.gamesWon + "\n" +
				"  gamesLost: " + this.gamesLost + "\n";
	}
}