package com.rossotti.basketball.util.service;

import com.rossotti.basketball.util.service.exception.PropertyException;
import com.rossotti.basketball.util.service.PropertyService.ClientSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyServiceTest {
	@Autowired
	private PropertyService propertyService;

	@Test
	public void getProperty_String_Valid() {
		String prop = propertyService.getProperty_String("accumulator.string.valid");
		Assert.assertEquals("validString", prop);
	}
	@Test(expected=PropertyException.class)
	public void getProperty_String_Empty() {
		propertyService.getProperty_String("accumulator.string.empty");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_String_Null() {
		propertyService.getProperty_String("accumulator.string.null");
	}

	@Test
	public void getProperty_Http_Valid() {
		String prop = propertyService.getProperty_Http("accumulator.http.valid");
		Assert.assertEquals("https://erikberg.com/nba/boxscore/", prop);
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Http_Invalid() {
		propertyService.getProperty_Http("accumulator.http.invalid");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Http_Empty() {
		propertyService.getProperty_Http("accumulator.http.empty");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Http_Null() {
		propertyService.getProperty_Http("accumulator.html.null");
	}

	@Test
	public void getProperty_Path_Valid() {
		String prop = propertyService.getProperty_Path("accumulator.path.valid");
		Assert.assertEquals("/usr/bin", prop);
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Path_Invalid() {
		propertyService.getProperty_Path("accumulator.path.invalid");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Path_Empty() {
		propertyService.getProperty_Path("accumulator.path.empty");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_Path_Null() {
		propertyService.getProperty_Path("accumulator.path.null");
	}

	@Test
	public void getProperty_ClientSource_Valid_Api() {
		ClientSource prop = propertyService.getProperty_ClientSource("accumulator.clientsource.valid.api");
		Assert.assertEquals(ClientSource.Api, prop);
	}
	@Test
	public void getProperty_ClientSource_Valid_File() {
		ClientSource prop = propertyService.getProperty_ClientSource("accumulator.clientsource.valid.file");
		Assert.assertEquals(ClientSource.File, prop);
	}
	@Test(expected=PropertyException.class)
	public void getProperty_ClientSource_Invalid() {
		propertyService.getProperty_ClientSource("accumulator.clientsource.invalid.enum");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_ClientSource_Empty() {
		propertyService.getProperty_ClientSource("accumulator.clientsource.invalid.empty");
	}
	@Test(expected=PropertyException.class)
	public void getProperty_ClientSource_Null() {
		propertyService.getProperty_ClientSource("accumulator.clientsource.invalid.null");
	}
}