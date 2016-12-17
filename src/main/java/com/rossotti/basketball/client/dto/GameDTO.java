package com.rossotti.basketball.client.dto;

public class GameDTO extends StatsDTO {
	public TeamDTO away_team;
	public TeamDTO home_team;
	public EventInfoDTO event_information;
	public int[] away_period_scores;
	public int[] home_period_scores;
	public BoxScoreDTO away_totals;
	public BoxScoreDTO home_totals;
	public BoxScorePlayerDTO[] away_stats;
	public BoxScorePlayerDTO[] home_stats;
	public OfficialDTO[] officials;
}
