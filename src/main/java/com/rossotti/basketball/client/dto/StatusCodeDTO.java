package com.rossotti.basketball.client.dto;

public class StatusCodeDTO {
	public enum StatusCode {
		Found,
		NotFound,
		ClientException,
		ServerException
	}
	private StatusCode statusCode;
	public StatusCode getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCode.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCode.NotFound;
	}
	public Boolean isClientException() {
		return statusCode == StatusCode.ClientException;
	}
	public Boolean isServerException() {
		return statusCode == StatusCode.ServerException;
	}
}
