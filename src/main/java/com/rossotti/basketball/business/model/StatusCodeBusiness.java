package com.rossotti.basketball.business.model;

public class StatusCodeBusiness {
	public enum StatusCode {
		Completed,
		ClientError,
		ServerError,
		RosterUpdate,
		OfficialError,
		TeamError
	}
	private StatusCode statusCode;
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public StatusCode getStatusCode() { return statusCode; }
	public Boolean isCompleted() {
		return statusCode == StatusCode.Completed;
	}
	public Boolean isClientError() {
		return statusCode == StatusCode.ClientError;
	}
	public Boolean isServerError() {
		return statusCode == StatusCode.ServerError;
	}
	public Boolean isRosterUpdate() {
		return statusCode == StatusCode.RosterUpdate;
	}
	public Boolean isOfficialError() {
		return statusCode == StatusCode.OfficialError;
	}
	public Boolean isTeamError() {
		return statusCode == StatusCode.TeamError;
	}

}