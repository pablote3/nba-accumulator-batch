package com.rossotti.basketball.business.model;

public class StatusCodeBusiness {
	public enum StatusCode {
		Completed,
		ClientError,
		ServerError,
		RosterUpdate,
		RosterComplete,
		RosterError,
		OfficialError,
		TeamError
	}
	private StatusCode statusCode;
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isCompleted() {
		return statusCode == StatusCode.Completed;
	}
	public Boolean isClientError() {
		return statusCode == StatusCode.ClientError;
	}
	public Boolean isServerError() {
		return statusCode == StatusCode.ServerError;
	}
}