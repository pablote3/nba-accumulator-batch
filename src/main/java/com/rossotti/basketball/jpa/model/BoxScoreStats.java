package com.rossotti.basketball.jpa.model;

import javax.persistence.Column;

public class BoxScoreStats {

	@Column(name="minutes")
	private Short minutes;
	public Short getMinutes() {
		return minutes;
	}
	public void setMinutes(Short minutes) {
		this.minutes = minutes;
	}

	@Column(name="points")
	private Short points;
	public Short getPoints() {
		return points;
	}
	public void setPoints(Short points) {
		this.points = points;
	}

	@Column(name="assists")
	private Short assists;
	public Short getAssists() {
		return assists;
	}
	public void setAssists(Short assists) {
		this.assists = assists;
	}

	@Column(name="turnovers")
	private Short turnovers;
	public Short getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(Short turnovers) {
		this.turnovers = turnovers;
	}

	@Column(name="steals")
	private Short steals;
	public Short getSteals() {
		return steals;
	}
	public void setSteals(Short steals) {
		this.steals = steals;
	}

	@Column(name="blocks")
	private Short blocks;
	public Short getBlocks() {
		return blocks;
	}
	public void setBlocks(Short blocks) {
		this.blocks = blocks;
	}

	@Column(name="fieldGoalAttempts")
	private Short fieldGoalAttempts;
	public Short getFieldGoalAttempts() {
		return fieldGoalAttempts;
	}
	public void setFieldGoalAttempts(Short fieldGoalAttempts) {
		this.fieldGoalAttempts = fieldGoalAttempts;
	}

	@Column(name="fieldGoalMade")
	private Short fieldGoalMade;
	public Short getFieldGoalMade() {
		return fieldGoalMade;
	}
	public void setFieldGoalMade(Short fieldGoalMade) {
		this.fieldGoalMade = fieldGoalMade;
	}

	@Column(name="fieldGoalPercent")
	private Float fieldGoalPercent;
	public Float getFieldGoalPercent() {
		return fieldGoalPercent;
	}
	public void setFieldGoalPercent(Float fieldGoalPercent) {
		this.fieldGoalPercent = fieldGoalPercent;
	}

	@Column(name="threePointAttempts")
	private Short threePointAttempts;
	public Short getThreePointAttempts() {
		return threePointAttempts;
	}
	public void setThreePointAttempts(Short threePointAttempts) {
		this.threePointAttempts = threePointAttempts;
	}

	@Column(name="threePointMade")
	private Short threePointMade;
	public Short getThreePointMade() {
		return threePointMade;
	}
	public void setThreePointMade(Short threePointMade) {
		this.threePointMade = threePointMade;
	}

	@Column(name="threePointPercent")
	private Float threePointPercent;
	public Float getThreePointPercent() {
		return threePointPercent;
	}
	public void setThreePointPercent(Float threePointPercent) {
		this.threePointPercent = threePointPercent;
	}

	@Column(name="freeThrowAttempts")
	private Short freeThrowAttempts;
	public Short getFreeThrowAttempts() {
		return freeThrowAttempts;
	}
	public void setFreeThrowAttempts(Short freeThrowAttempts) {
		this.freeThrowAttempts = freeThrowAttempts;
	}

	@Column(name="freeThrowMade")
	private Short freeThrowMade;
	public Short getFreeThrowMade() {
		return freeThrowMade;
	}
	public void setFreeThrowMade(Short freeThrowMade) {
		this.freeThrowMade = freeThrowMade;
	}

	@Column(name="freeThrowPercent")
	private Float freeThrowPercent;
	public Float getFreeThrowPercent() {
		return freeThrowPercent;
	}
	public void setFreeThrowPercent(Float freeThrowPercent) {
		this.freeThrowPercent = freeThrowPercent;
	}

	@Column(name="reboundsOffense")
	private Short reboundsOffense;
	public Short getReboundsOffense() {
		return reboundsOffense;
	}
	public void setReboundsOffense(Short reboundsOffense) {
		this.reboundsOffense = reboundsOffense;
	}

	@Column(name="reboundsDefense")
	private Short reboundsDefense;
	public Short getReboundsDefense() {
		return reboundsDefense;
	}
	public void setReboundsDefense(Short reboundsDefense) {
		this.reboundsDefense = reboundsDefense;
	}

	@Column(name="personalFouls")
	private Short personalFouls;
	public Short getPersonalFouls() {
		return personalFouls;
	}
	public void setPersonalFouls(Short personalFouls) {
		this.personalFouls = personalFouls;
	}
}
