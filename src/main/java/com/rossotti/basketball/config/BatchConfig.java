package com.rossotti.basketball.config;

import com.rossotti.basketball.batch.GameProcessor;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@Import(PersistenceConfig.class)
public class BatchConfig {

	private final PersistenceConfig persistenceConfig;

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

	@Autowired
	public BatchConfig(PersistenceConfig persistenceConfig, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.persistenceConfig = persistenceConfig;
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job scoreGameJob() {
		return jobBuilderFactory.get("scoreGameJob")
			.incrementer(new RunIdIncrementer())
			.flow(step1())
			.end()
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Game, Game> chunk(1)
			.reader(gameReader())
			.processor(gameProcessor())
			.writer(gameWriter())
			.transactionManager(persistenceConfig.transactionManager())
			.build();
	}

	@Bean
	public ItemReader<Game> gameReader() {
		logger.info("ItemReader - begin");
		JpaPagingItemReader<Game> reader = new JpaPagingItemReader<>();

//		LocalDate gameDate = LocalDate.now().minusDays(1);
		LocalDate gameDate = LocalDate.of(2016, 10, 25);

		LocalDateTime fromDateTime = DateTimeConverter.getLocalDateTimeMin(gameDate);
		LocalDateTime toDateTime = DateTimeConverter.getLocalDateTimeMax(gameDate);
		String sql = "select g from Game g where gameDateTime >= :fromDateTime and gameDateTime <= :toDateTime";
//		String sql = "select g from Game g where id = 4929";

		Map parameterValues = new HashMap<>();
		parameterValues.put("fromDateTime", fromDateTime);
		parameterValues.put("toDateTime", toDateTime);

		reader.setQueryString(sql);
		reader.setParameterValues(parameterValues);
		reader.setEntityManagerFactory(persistenceConfig.entityManagerFactory().getNativeEntityManagerFactory());
		logger.info("ItemReader - end");
		return reader;
	}

	@Bean
	public GameProcessor gameProcessor() {
		return new GameProcessor();
	}

	@Bean
	public ItemWriter<Game> gameWriter() {
		logger.info("ItemWriter - begin");
		JpaItemWriter<Game> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(persistenceConfig.entityManagerFactory().getNativeEntityManagerFactory());
		logger.info("ItemWriter - end");
		return writer;
	}
}