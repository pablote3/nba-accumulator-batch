package com.rossotti.basketball.client.dto;

import org.joda.time.DateTime;

public class StandingsDTO extends StatusCodeDTO {
	public DateTime standings_date;
	public StandingDTO[] standing;
}
