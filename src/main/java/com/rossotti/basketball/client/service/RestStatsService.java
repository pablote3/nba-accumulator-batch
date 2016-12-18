package com.rossotti.basketball.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.*;
import com.rossotti.basketball.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Service
public class RestStatsService {
	private final PropertyService propertyService;

	private final RestClientService restClientService;

	private final Logger logger = LoggerFactory.getLogger(RestStatsService.class);
	ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	@Autowired
	public RestStatsService(RestClientService restClientService, PropertyService propertyService) {
		this.restClientService = restClientService;
		this.propertyService = propertyService;
	}

	public GameDTO retrieveBoxScore(String event) {
		GameDTO gameDTO = new GameDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlBoxScore");
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);

			StatusCodeDTO statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCodeDTO.Found)) {
				gameDTO = objectMapper.readValue(entity.getBody(), GameDTO.class);
			}
			gameDTO.setStatusCode(statusCode);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			gameDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			gameDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return gameDTO;
	}

	public StandingsDTO retrieveStandings(String event) {
		StandingsDTO standingsDTO = new StandingsDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlStandings");
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);

			StatusCodeDTO statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCodeDTO.Found)) {
				standingsDTO = objectMapper.readValue(entity.getBody(), StandingsDTO.class);
			}
			standingsDTO.setStatusCode(statusCode);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return standingsDTO;
	}

	public RosterDTO retrieveRoster(String event, LocalDate asOfDate) {
		RosterDTO rosterDTO = new RosterDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlRoster");
			String file = propertyService.getProperty_Path("xmlstats.fileRoster") + "/" + event + "-" + DateTimeUtil.getStringDateNaked(asOfDate) + ".json";
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);

			StatusCodeDTO statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCodeDTO.Found)) {
				rosterDTO = objectMapper.readValue(entity.getBody(), RosterDTO.class);
				OutputStream outputStream = new FileOutputStream(file, false);
				outputStream.write(entity.getBody());
				outputStream.close();
			}
			rosterDTO.setStatusCode(statusCode);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			rosterDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			rosterDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return rosterDTO;
	}

	private StatusCodeDTO getStatusCode(ResponseEntity entity) {
		if (entity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			logger.info("Invalid token supplied on client request - HTTP Status = " + entity.getStatusCode());
			return StatusCodeDTO.NotFound;
		}
		else if (entity.getStatusCode() != HttpStatus.OK) {
			logger.info("Unable to retrieve client request - HTTP Status = " + entity.getStatusCode());
			return StatusCodeDTO.NotFound;
		}
		else {
			return StatusCodeDTO.Found;
		}
	}
}