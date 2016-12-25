package com.rossotti.basketball.business.model;

public class BusinessStatus {

	private BusinessStatusCode businessStatusCode;
	public void setBusinessStatusCode(BusinessStatusCode businessStatusCode) {
		this.businessStatusCode = businessStatusCode;
	}
	public Boolean isCompleted() {
		return businessStatusCode == BusinessStatusCode.Completed;
	}
	public Boolean isClientError() {
		return businessStatusCode == BusinessStatusCode.ClientError;
	}
	public Boolean isServerError() {
		return businessStatusCode == BusinessStatusCode.ServerError;
	}

	public enum BusinessStatusCode {
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