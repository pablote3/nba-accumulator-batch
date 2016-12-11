package com.rossotti.basketball.jpa.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="team", uniqueConstraints=@UniqueConstraint(columnNames={"teamKey", "fromDate", "toDate"}))
public class Team extends AbstractDomainClass {

	public Team() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public Team(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@OneToMany(mappedBy="team", fetch = FetchType.LAZY)
	private List<Standing> standings = new ArrayList<>();
	public List<Standing> getStandings() {
		return standings;
	}
	public void setStandings(List<Standing> standings) {
		this.standings = standings;
	}

	@OneToMany(mappedBy="team", fetch = FetchType.LAZY)
	private List<RosterPlayer> rosterPlayers = new ArrayList<>();
	public List<RosterPlayer> getRosterPlayers() {
		return rosterPlayers;
	}
	public void setRosterPlayers(List<RosterPlayer> rosterPlayers) {
		this.rosterPlayers = rosterPlayers;
	}

	@OneToMany(mappedBy="team", fetch = FetchType.LAZY)
	private List<BoxScore> boxScores = new ArrayList<>();
	public List<BoxScore> getBoxScores() {
		return boxScores;
	}
	public void setBoxScores(List<BoxScore> boxScores) {
		this.boxScores = boxScores;
	}

	@Column(name="teamKey", length=35, nullable=false)
	private String teamKey;
	public String getTeamKey() {
		return teamKey;
	}
	public void setTeamKey(String teamKey) {
		this.teamKey = teamKey;
	}

	@Column(name="fromDate", nullable=false)
	private LocalDate fromDate;
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	private LocalDate toDate;
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Column(name="firstName", length=15, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="lastName", length=20, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="fullName", length=35, nullable=false)
	private String fullName;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name="abbr", length=5, nullable=false)
	private String abbr;
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="conference", length=4, nullable=false)
	private Conference conference;
	public Conference getConference() {
		return conference;
	}
	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public enum Conference {
		East,
		West
	}

	@Enumerated(EnumType.STRING)
	@Column(name="division", length=9, nullable=false)
	private Division division;
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}

	public enum Division {
		Atlantic,
		Central,
		Southeast,
		Southwest,
		Northwest,
		Pacific
	}

	@Column(name="siteName", length=30, nullable=false)
	private String siteName;
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name="city", length=15, nullable=false)
	private String city;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="state", length=2, nullable=false)
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String toString() {
		return ("\n" + "  id: " + this.id + "\n") +
				"  teamKey: " + this.teamKey + "\n" +
				"  fromDate: " + this.fromDate + "\n" +
				"  toDate: " + this.toDate + "\n";
	}
}