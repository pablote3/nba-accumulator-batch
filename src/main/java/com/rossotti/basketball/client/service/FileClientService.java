package com.rossotti.basketball.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatsDTO.StatusCodeDTO;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileClientService {

	ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	public StatsDTO retrieveStats(String stringPath, String event, StatsDTO statsDTO) {
		String jsonEvent = event + ".json";
		Path path = Paths.get(stringPath).resolve(jsonEvent);
		InputStreamReader baseJson = null;
		try {
			File file = path.toFile();
			InputStream inputStreamJson = new FileInputStream(file);
			baseJson = new InputStreamReader(inputStreamJson, StandardCharsets.UTF_8);
			statsDTO = objectMapper.readValue(baseJson, statsDTO.getClass());
			statsDTO.setStatusCode(StatusCodeDTO.Found);
		} catch (FileNotFoundException fnf) {
			statsDTO.setStatusCode(StatusCodeDTO.NotFound);
			fnf.printStackTrace();
		} catch (IOException ioe) {
			statsDTO.setStatusCode(StatusCodeDTO.ClientException);
			ioe.printStackTrace();
		}
		finally {
			try {
				if (baseJson != null)
					baseJson.close();
			} catch (IOException ioe) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				ioe.printStackTrace();
			}
		}
		return statsDTO;
	}
}