package com.rossotti.basketball.util.service;

import com.rossotti.basketball.util.service.exception.PropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Configuration
@PropertySource("classpath:/service.properties")
public class PropertyService {
	private final Environment env;

	@Autowired
	public PropertyService(Environment env) {
		this.env = env;
	}

	public enum ClientSource {
		File,
		Api
	}

	public String getProperty_String(String propertyName) {
		String property = env.getProperty(propertyName);
		if (StringUtils.isEmpty(property)) {
			throw new PropertyException(propertyName);
		}
		return property;
	}

	public String getProperty_Http(String propertyName) {
		String http = getProperty_String(propertyName);
		if (!StringUtils.startsWithIgnoreCase(http, "https://")) {
			throw new PropertyException(propertyName);
		}
		return http;
	}

	public String getProperty_Path(String propertyName) {
		String path = getProperty_String(propertyName);
		if (!new File(path).exists()) {
			throw new PropertyException(propertyName);
		}
		return path;
	}

	public ClientSource getProperty_ClientSource(String propertyName) {
		String property = getProperty_String(propertyName);
		AtomicReference<ClientSource> clientSource = new AtomicReference<>();
		try {
			clientSource.set(ClientSource.valueOf(property));
		} catch (IllegalArgumentException e) {
			throw new PropertyException(propertyName);
		}
		return clientSource.get();
	}
}