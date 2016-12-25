package com.rossotti.basketball.business.model;

import com.rossotti.basketball.jpa.model.Standing;

import java.util.List;

public class BusinessStandings extends StatusCodeBusiness {

	private List<Standing> standings;
	public List<Standing> getStandings() {
		return standings;
	}
	public void setStandings(List<Standing> standings) {
		this.standings = standings;
	}
}
