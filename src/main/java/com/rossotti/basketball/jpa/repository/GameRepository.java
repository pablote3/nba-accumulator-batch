package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends Repository<Game, Long> {

	List<Game> findAll();

	Game findOne(Long id);

	void save(Game player);

	void delete(Long id);

	String findByTeamKeyAndFromDateAndToDate =
			"select g from Game g " +
			"inner join g.boxScores bs " +
			"inner join bs.team t " +
			"where g.gameDateTime >= :fromDateTime " +
			"and g.gameDateTime <= :toDateTime " +
			"and t.teamKey = :teamKey";

	@Query(findByTeamKeyAndFromDateAndToDate)
	Game findByTeamKeyAndFromDateAndToDate(@Param("teamKey") String teamKey, @Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

	String findByTeamKeyAndFromDateAndToDateSeason =
			"select g from Game g " +
			"left join g.boxScores bs " +
			"inner join bs.team t " +
			"where g.gameDateTime >= :fromDateTime " +
			"and g.gameDateTime <= :toDateTime " +
			"and (g.status = 'Completed' " +
			"or g.status = 'Scheduled') " +
			"and t.teamKey = :teamKey " +
			"order by g.gameDateTime asc";

	@Query(findByTeamKeyAndFromDateAndToDateSeason)
	List<Game> findByTeamKeyAndFromDateAndToDateSeason(@Param("teamKey") String teamKey, @Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

	String findByFromDateAndToDate =
			"from Game " +
			"where gameDateTime >= :fromDateTime " +
			"and gameDateTime <= :toDateTime " +
			"order by status desc, gameDateTime asc";

	@Query(findByFromDateAndToDate)
	List<Game> findByFromDateAndToDate(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

	String findPreviousByTeamKeyAndAsOfDate =
			"select g.gameDateTime from Game g " +
			"left join g.boxScores bs " +
			"inner join bs.team t " +
			"where g.gameDateTime <= :asOfDateTime " +
			"and g.status = 'Completed' " +
			"and t.teamKey = :teamKey " +
			"order by gameDateTime desc";

	@Query(findPreviousByTeamKeyAndAsOfDate)
	List<LocalDateTime> findPreviousByTeamKeyAndAsOfDate(@Param("teamKey") String teamKey, @Param("asOfDateTime") LocalDateTime asOfDateTime);
}