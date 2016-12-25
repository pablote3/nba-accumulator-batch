package com.rossotti.basketball.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.util.service.exception.FileException;
import com.rossotti.basketball.util.service.exception.PropertyException;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.util.service.FileService;
import com.rossotti.basketball.util.service.PropertyService;
import com.rossotti.basketball.client.dto.StatusCodeDTO.StatusCode;
import com.rossotti.basketball.util.function.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class RestStatsService {

	private final PropertyService propertyService;
	private final RestClientService restClientService;
	private final FileService fileService;

	private final Logger logger = LoggerFactory.getLogger(RestStatsService.class);
	ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	@Autowired
	public RestStatsService(RestClientService restClientService, PropertyService propertyService, FileService fileService) {
		this.restClientService = restClientService;
		this.propertyService = propertyService;
		this.fileService = fileService;
	}

	public GameDTO retrieveBoxScore(String event, boolean persist) {
		GameDTO gameDTO = new GameDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlBoxScore");
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);
			if (persist) {
				String fileName = propertyService.getProperty_Path("xmlstats.fileBoxScore") + "/" + event + ".json";
				fileService.fileStreamWriter(fileName, entity.getBody());
			}
			StatusCode statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCode.Found)) {
				gameDTO = objectMapper.readValue(entity.getBody(), GameDTO.class);
			}
			gameDTO.setStatusCode(statusCode);
		}
		catch (FileException fe) {
			logger.info("File exception = " + fe);
			gameDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			gameDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			gameDTO.setStatusCode(StatusCode.ServerException);
		}
		return gameDTO;
	}

	public StandingsDTO retrieveStandings(String event, boolean persist) {
		StandingsDTO standingsDTO = new StandingsDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlStandings");
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);
			if (persist) {
				String fileName = propertyService.getProperty_Path("xmlstats.fileStandings") + "/" + event + ".json";
				fileService.fileStreamWriter(fileName, entity.getBody());
			}
			StatusCode statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCode.Found)) {
				standingsDTO = objectMapper.readValue(entity.getBody(), StandingsDTO.class);

			}
			standingsDTO.setStatusCode(statusCode);
		}
		catch (FileException fe) {
			logger.info("File exception = " + fe);
			standingsDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			standingsDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			standingsDTO.setStatusCode(StatusCode.ServerException);
		}
		return standingsDTO;
	}

	public RosterDTO retrieveRoster(String event, boolean persist, LocalDate asOfDate) {
		RosterDTO rosterDTO = new RosterDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlRoster");
			String eventUrl = baseUrl + event + ".json";
			ResponseEntity<byte[]> entity = restClientService.getJson(eventUrl);

			StatusCode statusCode = getStatusCode(entity);
			if (statusCode.equals(StatusCode.Found)) {
				rosterDTO = objectMapper.readValue(entity.getBody(), RosterDTO.class);
				if (persist) {
					String fileName = propertyService.getProperty_Path("xmlstats.fileRoster") + "/" + event + "-" + DateTimeConverter.getStringDateNaked(asOfDate) + ".json";
					fileService.fileStreamWriter(fileName, entity.getBody());
				}
			}
			rosterDTO.setStatusCode(statusCode);
		}
		catch (FileException fe) {
			logger.info("File exception = " + fe);
			rosterDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (IOException ioe) {
			logger.info("IO exception = " + ioe);
			rosterDTO.setStatusCode(StatusCode.ServerException);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			rosterDTO.setStatusCode(StatusCode.ServerException);
		}
		return rosterDTO;
	}

	private StatusCode getStatusCode(ResponseEntity entity) {
		if (entity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			logger.info("Invalid token supplied on client request - HTTP Status = " + entity.getStatusCode());
			return StatusCode.NotFound;
		}
		else if (entity.getStatusCode() != HttpStatus.OK) {
			logger.info("Unable to retrieve client request - HTTP Status = " + entity.getStatusCode());
			return StatusCode.NotFound;
		}
		else {
			return StatusCode.Found;
		}
	}
}