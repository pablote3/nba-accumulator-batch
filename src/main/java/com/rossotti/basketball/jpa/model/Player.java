package com.rossotti.basketball.jpa.model;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="player", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "birthdate"}))
public class Player extends AbstractDomainClass {

	public Player() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public Player(AbstractDomainClass.StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@OneToMany(mappedBy="player", fetch = FetchType.LAZY)
	private List<RosterPlayer> rosterPlayers = new ArrayList<>();
	public List<RosterPlayer> getRosterPlayers()  {
		return rosterPlayers;
	}
	public void setRosterPlayers(List<RosterPlayer> rosterPlayers)  {
		this.rosterPlayers = rosterPlayers;
	}

	@Column(name="lastName", length=20, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=20, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="birthdate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate birthdate;
	public LocalDate getBirthdate()  {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name="displayName", length=40, nullable=false)
	private String displayName;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name="height")
	private Short height;
	public Short getHeight() {
		return height;
	}
	public void setHeight(Short height) {
		this.height = height;
	}

	@Column(name="weight")
	private Short weight;
	public Short getWeight() {
		return weight;
	}
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	@Column(name="birthplace", length=40)
	private String birthplace;
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String toString() {
		return ("\n" + "  id: " + this.id + "\n") +
				"  lastName: " + this.lastName + "\n" +
				"  firstName: " + this.firstName + "\n" +
				"  birthdate: " + this.birthdate;
	}
}