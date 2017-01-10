package com.rossotti.basketball.jpa.model;

import com.rossotti.basketball.client.dto.BoxScoreDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="boxScore")
public class BoxScore extends AbstractDomainClass {

	public BoxScore() {}

	@ManyToOne()
	@JoinColumn(name="gameId", referencedColumnName="id", nullable=false)
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne()
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@OneToMany(mappedBy="boxScore", fetch = FetchType.LAZY, cascade= CascadeType.ALL)
	private List<BoxScorePlayer> boxScorePlayers = new ArrayList<>();
	public List<BoxScorePlayer> getBoxScorePlayers()  {
		return boxScorePlayers;
	}
	public void setBoxScorePlayers(List<BoxScorePlayer> boxScorePlayers)  {
		this.boxScorePlayers = boxScorePlayers;
	}
	public void addBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().add(boxScorePlayer);
	}

	@Enumerated(EnumType.STRING)
	@Column(name="location", length=5, nullable=false)
	private Location location;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public enum Location {
		Home,
		Away
	}

	@Enumerated(EnumType.STRING)
	@Column(name="result", length=4)
	private Result result;
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public enum Result {
		Win,
		Loss
	}

	@Column(name="pointsPeriod1")
	private Short pointsPeriod1;
	public Short getPointsPeriod1() {
		return pointsPeriod1;
	}
	public void setPointsPeriod1(Short pointsPeriod1) {
		this.pointsPeriod1 = pointsPeriod1;
	}

	@Column(name="pointsPeriod2")
	private Short pointsPeriod2;
	public Short getPointsPeriod2() {
		return pointsPeriod2;
	}
	public void setPointsPeriod2(Short pointsPeriod2) {
		this.pointsPeriod2 = pointsPeriod2;
	}

	@Column(name="pointsPeriod3")
	private Short pointsPeriod3;
	public Short getPointsPeriod3() {
		return pointsPeriod3;
	}
	public void setPointsPeriod3(Short pointsPeriod3) {
		this.pointsPeriod3 = pointsPeriod3;
	}

	@Column(name="pointsPeriod4")
	private Short pointsPeriod4;
	public Short getPointsPeriod4() {
		return pointsPeriod4;
	}
	public void setPointsPeriod4(Short pointsPeriod4) {
		this.pointsPeriod4 = pointsPeriod4;
	}

	@Column(name="pointsPeriod5")
	private Short pointsPeriod5;
	public Short getPointsPeriod5() {
		return pointsPeriod5;
	}
	public void setPointsPeriod5(Short pointsPeriod5) {
		this.pointsPeriod5 = pointsPeriod5;
	}

	@Column(name="pointsPeriod6")
	private Short pointsPeriod6;
	public Short getPointsPeriod6() {
		return pointsPeriod6;
	}
	public void setPointsPeriod6(Short pointsPeriod6) {
		this.pointsPeriod6 = pointsPeriod6;
	}

	@Column(name="pointsPeriod7")
	private Short pointsPeriod7;
	public Short getPointsPeriod7() {
		return pointsPeriod7;
	}
	public void setPointsPeriod7(Short pointsPeriod7) {
		this.pointsPeriod7 = pointsPeriod7;
	}

	@Column(name="pointsPeriod8")
	private Short pointsPeriod8;
	public Short getPointsPeriod8() {
		return pointsPeriod8;
	}
	public void setPointsPeriod8(Short pointsPeriod8) {
		this.pointsPeriod8 = pointsPeriod8;
	}

	@Column(name="daysOff")
	private Short daysOff;
	public Short getDaysOff() {
		return daysOff;
	}
	public void setDaysOff(Short daysOff) {
		this.daysOff = daysOff;
	}

	@Embedded
	private BoxScoreStats boxScoreStats;
	public BoxScoreStats getBoxScoreStats() {
		return boxScoreStats;
	}
	public void setBoxScoreStats(BoxScoreStats boxScoreStats) {
		this.boxScoreStats = boxScoreStats;
	}

	public String toString() {
		return ("  id: " + this.id + "\n" +
				"  location: " + this.location);
	}

	public void updateTotals(BoxScoreDTO stats) {
		if (this.getBoxScoreStats() == null) {
			this.setBoxScoreStats(new BoxScoreStats());
		}
		this.getBoxScoreStats().setMinutes(stats.getMinutes());
		this.getBoxScoreStats().setPoints(stats.getPoints());
		this.getBoxScoreStats().setAssists(stats.getAssists());
		this.getBoxScoreStats().setTurnovers(stats.getTurnovers());
		this.getBoxScoreStats().setSteals(stats.getSteals());
		this.getBoxScoreStats().setBlocks(stats.getBlocks());
		this.getBoxScoreStats().setFieldGoalAttempts(stats.getField_goals_attempted());
		this.getBoxScoreStats().setFieldGoalMade(stats.getField_goals_made());
		this.getBoxScoreStats().setFieldGoalPercent(stats.getField_goal_percentage());
		this.getBoxScoreStats().setThreePointAttempts(stats.getThree_point_field_goals_attempted());
		this.getBoxScoreStats().setThreePointMade(stats.getThree_point_field_goals_made());
		this.getBoxScoreStats().setThreePointPercent(stats.getThree_point_percentage());
		this.getBoxScoreStats().setFreeThrowAttempts(stats.getFree_throws_attempted());
		this.getBoxScoreStats().setFreeThrowMade(stats.getFree_throws_made());
		this.getBoxScoreStats().setFreeThrowPercent(stats.getFree_throw_percentage());
		this.getBoxScoreStats().setReboundsOffense(stats.getOffensive_rebounds());
		this.getBoxScoreStats().setReboundsDefense(stats.getDefensive_rebounds());
		this.getBoxScoreStats().setPersonalFouls(stats.getPersonal_fouls());
	}

	public void updatePeriodScores(int[] periodScores) {
		this.setPointsPeriod1((short)periodScores[0]);
		this.setPointsPeriod2((short)periodScores[1]);
		this.setPointsPeriod3((short)periodScores[2]);
		this.setPointsPeriod4((short)periodScores[3]);
		if(periodScores.length > 4)
			this.setPointsPeriod5((short)periodScores[4]);
		if(periodScores.length > 5)
			this.setPointsPeriod6((short)periodScores[5]);
		if(periodScores.length > 6)
			this.setPointsPeriod7((short)periodScores[6]);
		if(periodScores.length > 7)
			this.setPointsPeriod8((short)periodScores[7]);
	}
}