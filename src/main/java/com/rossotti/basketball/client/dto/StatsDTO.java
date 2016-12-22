package com.rossotti.basketball.client.dto;

public class StatsDTO {
	public enum StatusCodeDTO {
		Found,
		NotFound,
		ClientException,
		ServerException
	}
	private StatusCodeDTO statusCode;
	public StatusCodeDTO getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCodeDTO statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCodeDTO.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCodeDTO.NotFound;
	}
	public Boolean isClientException() {
		return statusCode == StatusCodeDTO.ClientException;
	}
	public Boolean isServerException() {
		return statusCode == StatusCodeDTO.ServerException;
	}
}
