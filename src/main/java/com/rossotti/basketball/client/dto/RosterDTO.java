package com.rossotti.basketball.client.dto;

@SuppressWarnings("CanBeFinal")
public class RosterDTO extends StatusCodeDTO {
	public TeamDTO team;
	public RosterPlayerDTO[] players;
}