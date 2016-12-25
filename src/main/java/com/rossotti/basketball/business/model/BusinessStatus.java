package com.rossotti.basketball.business.model;

public class BusinessStatus {

	private StatusCodeBusiness statusCodeBusiness;
	public void setBusinessStatusCode(StatusCodeBusiness statusCodeBusiness) {
		this.statusCodeBusiness = statusCodeBusiness;
	}
	public Boolean isCompleted() {
		return statusCodeBusiness == StatusCodeBusiness.Completed;
	}
	public Boolean isClientError() {
		return statusCodeBusiness == StatusCodeBusiness.ClientError;
	}
	public Boolean isServerError() {
		return statusCodeBusiness == StatusCodeBusiness.ServerError;
	}

	public enum StatusCodeBusiness {
		Completed,
		ClientError,
		ServerError,
		RosterUpdate,
		RosterComplete,
		RosterError,
		OfficialError,
		TeamError
	}
}