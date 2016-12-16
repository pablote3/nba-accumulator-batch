package com.rossotti.basketball.client.dto;

public class BoxScoreDTO {
	private Short minutes;
	private Short points;
	private Short assists;
	private Short turnovers;
	private Short steals;
	private Short blocks;
	private Short field_goals_attempted;
	private Short field_goals_made;
	private Short three_point_field_goals_attempted;
	private Short three_point_field_goals_made;
	private Short free_throws_attempted;
	private Short free_throws_made;
	private Short defensive_rebounds;
	private Short offensive_rebounds;
	private Short personal_fouls;
	private String team_abbreviation;
	private Float field_goal_percentage;
	private Float three_point_percentage;
	private Float free_throw_percentage;

	public Short getMinutes() {
		return minutes;
	}
	public void setMinutes(Short minutes) {
		this.minutes = minutes;
	}
	public Short getPoints() {
		return points;
	}
	public void setPoints(Short points) {
		this.points = points;
	}
	public Short getAssists() {
		return assists;
	}
	public void setAssists(Short assists) {
		this.assists = assists;
	}
	public Short getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(Short turnovers) {
		this.turnovers = turnovers;
	}
	public Short getSteals() {
		return steals;
	}
	public void setSteals(Short steals) {
		this.steals = steals;
	}
	public Short getBlocks() {
		return blocks;
	}
	public void setBlocks(Short blocks) {
		this.blocks = blocks;
	}
	public Short getField_goals_attempted() {
		return field_goals_attempted;
	}
	public void setField_goals_attempted(Short field_goals_attempted) {
		this.field_goals_attempted = field_goals_attempted;
	}
	public Short getField_goals_made() {
		return field_goals_made;
	}
	public void setField_goals_made(Short field_goals_made) {
		this.field_goals_made = field_goals_made;
	}
	public Short getThree_point_field_goals_attempted() {
		return three_point_field_goals_attempted;
	}
	public void setThree_point_field_goals_attempted(Short three_point_field_goals_attempted) {
		this.three_point_field_goals_attempted = three_point_field_goals_attempted;
	}
	public Short getThree_point_field_goals_made() {
		return three_point_field_goals_made;
	}
	public void setThree_point_field_goals_made(Short three_point_field_goals_made) {
		this.three_point_field_goals_made = three_point_field_goals_made;
	}
	public Short getFree_throws_attempted() {
		return free_throws_attempted;
	}
	public void setFree_throws_attempted(Short free_throws_attempted) {
		this.free_throws_attempted = free_throws_attempted;
	}
	public Short getFree_throws_made() {
		return free_throws_made;
	}
	public void setFree_throws_made(Short free_throws_made) {
		this.free_throws_made = free_throws_made;
	}
	public Short getDefensive_rebounds() {
		return defensive_rebounds;
	}
	public void setDefensive_rebounds(Short defensive_rebounds) {
		this.defensive_rebounds = defensive_rebounds;
	}
	public Short getOffensive_rebounds() {
		return offensive_rebounds;
	}
	public void setOffensive_rebounds(Short offensive_rebounds) {
		this.offensive_rebounds = offensive_rebounds;
	}
	public Short getPersonal_fouls() {
		return personal_fouls;
	}
	public void setPersonal_fouls(Short personal_fouls) {
		this.personal_fouls = personal_fouls;
	}
	public String getTeam_abbreviation() {
		return team_abbreviation;
	}
	public void setTeam_abbreviation(String team_abbreviation) {
		this.team_abbreviation = team_abbreviation;
	}
	public Float getField_goal_percentage() {
		return field_goal_percentage;
	}
	public void setField_goal_percentage(Float field_goal_percentage) {
		this.field_goal_percentage = field_goal_percentage;
	}
	public Float getThree_point_percentage() {
		return three_point_percentage;
	}
	public void setThree_point_percentage(Float three_point_percentage) {
		this.three_point_percentage = three_point_percentage;
	}
	public Float getFree_throw_percentage() {
		return free_throw_percentage;
	}
	public void setFree_throw_percentage(Float free_throw_percentage) {
		this.free_throw_percentage = free_throw_percentage;
	}
}
