package com.rossotti.basketball.business.model;

import com.rossotti.basketball.jpa.model.RosterPlayer;
import java.util.List;

public class RosterPlayerBusiness extends StatusCodeBusiness {

	private List<RosterPlayer> rosterPlayers;
	public List<RosterPlayer> getRosterPlayers() {
		return rosterPlayers;
	}
	public void setRosterPlayers(List<RosterPlayer> rosterPlayers) {
		this.rosterPlayers = rosterPlayers;
	}
}
