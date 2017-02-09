package com.rossotti.basketball.batch;

import com.rossotti.basketball.app.service.GameAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;

public class GameCountTasklet implements Tasklet {

	@Autowired
	private GameAppService gameAppService;

	private final Logger logger = LoggerFactory.getLogger(GameCountTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LocalDate asOfDate = LocalDate.of(2016, 10, 25);

		chunkContext
			.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext()
			.put("asOfDate", asOfDate);

		int countByAsOfDate = gameAppService.findCountByAsOfDate(asOfDate);
		logger.info(countByAsOfDate + " Games Scheduled for " + asOfDate);
		return RepeatStatus.FINISHED;
	}
}