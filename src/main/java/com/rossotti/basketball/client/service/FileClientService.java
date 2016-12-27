package com.rossotti.basketball.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO.StatusCode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileClientService {

	private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

	public StatusCodeDTO retrieveStats(String stringPath, String event, StatusCodeDTO statusCodeDTO) {
		String jsonEvent = event + ".json";
		Path path = Paths.get(stringPath).resolve(jsonEvent);
		InputStreamReader baseJson = null;
		try {
			File file = path.toFile();
			InputStream inputStreamJson = new FileInputStream(file);
			baseJson = new InputStreamReader(inputStreamJson, StandardCharsets.UTF_8);
			statusCodeDTO = objectMapper.readValue(baseJson, statusCodeDTO.getClass());
			statusCodeDTO.setStatusCode(StatusCode.Found);
		} catch (FileNotFoundException fnf) {
			statusCodeDTO.setStatusCode(StatusCode.NotFound);
			fnf.printStackTrace();
		} catch (IOException ioe) {
			statusCodeDTO.setStatusCode(StatusCode.ClientException);
			ioe.printStackTrace();
		}
		finally {
			try {
				if (baseJson != null)
					baseJson.close();
			} catch (IOException ioe) {
				statusCodeDTO.setStatusCode(StatusCode.ClientException);
				ioe.printStackTrace();
			}
		}
		return statusCodeDTO;
	}
}