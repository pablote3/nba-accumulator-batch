package com.rossotti.basketball.batch;

import com.rossotti.basketball.batch.exception.SkipStepException;
import com.rossotti.basketball.business.model.StandingsBusiness;
import com.rossotti.basketball.business.service.StandingBusService;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.joda.time.LocalDate;

public class StandingsTasklet implements Tasklet {

	@Autowired
	private StandingBusService standingBusService;

	private final Logger logger = LoggerFactory.getLogger(StandingsTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LocalDate gameDate = (LocalDate)chunkContext
			.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext()
			.get("asOfDate");

		String asOfDate = DateTimeConverter.getStringDate(gameDate);
		StandingsBusiness standingsBusiness = standingBusService.rankStandings(asOfDate);

		if (standingsBusiness.isCompleted()) {
			logger.info("RankStandings Complete for " + asOfDate);
			return RepeatStatus.FINISHED;
		}
		else {
			logger.info("RankStandings problem - status code: " + standingsBusiness.getStatusCode());
			throw new SkipStepException("RankStandings problem - status code: " + standingsBusiness.getStatusCode());
		}
	}
}