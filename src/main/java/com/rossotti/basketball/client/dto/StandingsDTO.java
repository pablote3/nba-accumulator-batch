package com.rossotti.basketball.client.dto;

import java.time.ZonedDateTime;

public class StandingsDTO extends StatusCodeDTO {
	public ZonedDateTime standings_date;
	public StandingDTO[] standing;
}
