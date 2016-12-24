package com.rossotti.basketball.business.model;

import com.rossotti.basketball.jpa.model.Standing;

import java.util.List;

public class AppStandings {
	private AppStatus appStatus;
	public void setAppStatus(AppStatus appStatus) {
		this.appStatus = appStatus;
	}
	public Boolean isAppCompleted() {
		return appStatus == AppStatus.Completed;
	}
	public Boolean isAppClientError() {
		return appStatus == AppStatus.ClientError;
	}
	public Boolean isAppServerError() {
		return appStatus == AppStatus.ServerError;
	}
	private List<Standing> standings;
	public List<Standing> getStandings() {
		return standings;
	}
	public void setStandings(List<Standing> standings) {
		this.standings = standings;
	}
}
