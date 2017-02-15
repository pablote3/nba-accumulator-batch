package com.rossotti.basketball.jpa.model;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="official", uniqueConstraints=@UniqueConstraint(columnNames={"lastName", "firstName", "fromDate", "toDate"}))
public class Official extends AbstractDomainClass {

	public Official() {
		setStatusCode(StatusCodeDAO.Found);
	}

	public Official(StatusCodeDAO statusCode) {
		setStatusCode(statusCode);
	}

	@OneToMany(mappedBy="official", fetch = FetchType.LAZY)
	private List<GameOfficial> gameOfficials = new ArrayList<>();
	private List<GameOfficial> getGameOfficials() {
		return gameOfficials;
	}
	public void setGameOfficials(List<GameOfficial> gameOfficials) {
		this.gameOfficials = gameOfficials;
	}

	@Column(name="lastName", length=25, nullable=false)
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="firstName", length=25, nullable=false)
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="fromDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate fromDate;
	public LocalDate getFromDate()  {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name="toDate", nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
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

	public String toString() {
		return ("\r" + "  id: " + this.id + "\n") +
				"  lastName: " + this.lastName + "\n" +
				"  firstName: " + this.firstName + "\n" +
				"  fromDate: " + this.fromDate + "\n" +
				"  toDate: " + this.toDate + "\n";
	}
}