package com.rossotti.basketball.config;

import com.rossotti.basketball.batch.GameProcessor;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
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

@Configuration
@EnableBatchProcessing
@Import(PersistenceConfig.class)
public class BatchConfig {

	private final PersistenceConfig persistenceConfig;

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public BatchConfig(PersistenceConfig persistenceConfig, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.persistenceConfig = persistenceConfig;
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public ItemReader<Game> reader() {
		JpaPagingItemReader<Game> reader = new JpaPagingItemReader<>();

		LocalDate gameDate = LocalDate.now().minusDays(1);

		String minDateTime = DateTimeConverter.getStringDateTime(DateTimeConverter.getLocalDateTimeMin(gameDate));
		String maxDateTime = DateTimeConverter.getStringDateTime(DateTimeConverter.getLocalDateTimeMax(gameDate));
//		String sql = "select g from Game g where gameDateTime between '" + minDateTime + "' and '" + maxDateTime + "'";

//		String sql = "select g from Game g where gameDateTime between " + DateTimeConverter.getLocalDateTimeMin(gameDate) + " and " + DateTimeConverter.getLocalDateTimeMax(gameDate);
//		String sql = "select g from Game g where gameDateTime between " + gameDate + " and " + gameDate.plusDays(1);

		String sql = "select g from Game g where id = 5443";

		reader.setQueryString(sql);
		reader.setEntityManagerFactory(persistenceConfig.entityManagerFactory().getNativeEntityManagerFactory());
		return reader;
	}

//	private BeanPropertyRowMapper<GameReaderInput> gameMapper = new BeanPropertyRowMapper(GameReaderInput.class);

//	@Bean
//	public JdbcCursorItemReader<GameReaderInput> reader() {
//		JdbcCursorItemReader<GameReaderInput> reader = new JdbcCursorItemReader<GameReaderInput>();
//
//		LocalDate gameDate = LocalDate.now().minusDays(1);
//		String minDateTime = DateTimeConverter.getStringDateTime(DateTimeConverter.getLocalDateTimeMin(gameDate));
//		String maxDateTime = DateTimeConverter.getStringDateTime(DateTimeConverter.getLocalDateTimeMax(gameDate));
//
//		String sql = "select g.gameDateTime, t.teamKey, g.status " +
//					"from game g " +
//					"inner join boxScore as bs on g.id = bs.gameId " +
//					"inner join team as t on t.id = bs.teamId " +
//					"where g.gameDateTime between '" + minDateTime + "' and '" + maxDateTime + "' " +
//					"and bs.location = 'Home' " +
//					"order by g.gameDateTime asc";
//		reader.setSql(sql);
//		reader.setDataSource(persistenceConfig.dataSource());
//		reader.setRowMapper(new RowMapper<GameReaderInput>() {
//			@Override
//			public GameReaderInput mapRow(ResultSet rs, int rowNum) throws SQLException {
//				GameReaderInput rowObject = gameMapper.mapRow(rs, rowNum);
//				return rowObject;
//			}
//		});
//		return reader;
//	}

	@Bean
	public GameProcessor processor() {
		return new GameProcessor();
	}

	@Bean
	public ItemWriter<Game> writer() {
		JpaItemWriter<Game> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(persistenceConfig.entityManagerFactory().getNativeEntityManagerFactory());
		return writer;
	}

//	@Bean
//	public FlatFileItemWriter<GameReaderInput> writer() {
//		FlatFileItemWriter<GameReaderInput> writer = new FlatFileItemWriter<>();
//
//		writer.setResource(new FileSystemResource(new File("target/paulOut.txt")));
//		writer.setShouldDeleteIfExists(true);
//
//		BeanWrapperFieldExtractor<GameReaderInput> fieldExtractor = new BeanWrapperFieldExtractor<>();
//		fieldExtractor.setNames(new String[]{"gameDateTime", "teamKey", "status"});
//
//		DelimitedLineAggregator<GameReaderInput> lineAggregator = new DelimitedLineAggregator<>();
//		lineAggregator.setFieldExtractor(fieldExtractor);
//
//		writer.setLineAggregator(lineAggregator);
//		return writer;
//	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Game, Game> chunk(1)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.transactionManager(persistenceConfig.transactionManager())
			.build();
	}

	@Bean
	public Job importUserJob() {
		return jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
//			.listener(listener)
			.flow(step1())
			.end()
			.build();
	}
}