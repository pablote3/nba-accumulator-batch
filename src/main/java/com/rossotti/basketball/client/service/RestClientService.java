package com.rossotti.basketball.client.service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.*;
import com.rossotti.basketball.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Service
public class RestClientService {

	private final PropertyService propertyService;
	private final Logger logger = LoggerFactory.getLogger(RestClientService.class);

	@Autowired
	public RestClientService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public HttpEntity<String> getEntity() throws PropertyException {
		String accessToken = propertyService.getProperty_String("xmlstats.accessToken");
		String userAgent = propertyService.getProperty_String("xmlstats.userAgent");

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		headers.set(HttpHeaders.USER_AGENT, userAgent);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}

	public RestTemplate getRestTemplate() {
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		restTemplateBuilder.additionalMessageConverters(new MappingJackson2HttpMessageConverter());
		return restTemplateBuilder.build();
	}
}