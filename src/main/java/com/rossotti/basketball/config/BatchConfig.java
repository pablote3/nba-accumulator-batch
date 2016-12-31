package com.rossotti.basketball.config;

import com.rossotti.basketball.batch.model.GameReaderInput;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
@Import(PersistenceConfig.class)
public class BatchConfig {

	@Autowired
	private  PersistenceConfig persistenceConfig;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private BeanPropertyRowMapper<GameReaderInput> gameMapper = new BeanPropertyRowMapper(GameReaderInput.class);

	@Bean
	public JdbcCursorItemReader<GameReaderInput> reader() {
		JdbcCursorItemReader<GameReaderInput> reader = new JdbcCursorItemReader<GameReaderInput>();

		LocalDate gameDate = LocalDate.now().minusDays(1);
		LocalDateTime minDateTime = DateTimeConverter.getLocalDateTimeMin(gameDate);
		LocalDateTime maxDateTime = DateTimeConverter.getLocalDateTimeMax(gameDate);

		String sql = "select * from game where gameDateTime between '" + minDateTime + "' and '" + maxDateTime + "'";
		reader.setSql(sql);
		reader.setDataSource(persistenceConfig.dataSource());
		reader.setRowMapper(new RowMapper<GameReaderInput>() {
			@Override
			public GameReaderInput mapRow(ResultSet rs, int rowNum) throws SQLException {
				GameReaderInput rowObject = gameMapper.mapRow(rs, rowNum);
				return rowObject;
			}
		});
		return reader;
	}

//	@Bean
//	public PersonItemProcessor processor() {
//		return new PersonItemProcessor();
//	}

	@Bean
	public FlatFileItemWriter<Game> writer() {
		FlatFileItemWriter<Game> writer = new FlatFileItemWriter<>();

		writer.setResource(new FileSystemResource(new File("target/paulOut.txt")));
		writer.setShouldDeleteIfExists(true);

		BeanWrapperFieldExtractor<Game> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[]{"id"});

		DelimitedLineAggregator<Game> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setFieldExtractor(fieldExtractor);

		writer.setLineAggregator(lineAggregator);
		return writer;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<GameReaderInput, Game> chunk(10)
			.reader(reader())
//			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean
//	public Job importUserJob(JobCompletionNotificationListener listener) {
	public Job importUserJob() {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
//				.listener(listener)
				.flow(step1())
				.end()
				.build();
	}
}