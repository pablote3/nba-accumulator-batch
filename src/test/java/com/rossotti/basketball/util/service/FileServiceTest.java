package com.rossotti.basketball.util.service;

import com.rossotti.basketball.util.function.StreamConverter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {
	@Autowired
	private FileService fileService;

	@Ignore
	@Test
	public void writeFile_UTF8_Valid() {
		String input = "Test Valančiūnas";
		InputStream stream = new ByteArrayInputStream(input.getBytes());
		String fileName = "/home/pablote/pdrive/pwork/boot/testFile.txt";
		fileService.fileStreamWriter(fileName, StreamConverter.getBytes(stream));
		Assert.assertEquals("Test Valančiūnas", fileService.fileLineReader(fileName));
		fileService.fileDelete(fileName);
	}

	@Ignore
	@Test
	public void writeFile_RosterJson_Valid() {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/rosterClient.json");
		String fileName = "/home/pablote/pdrive/pwork/boot/testFile.txt";
		fileService.fileStreamWriter(fileName, StreamConverter.getBytes(baseJson));
		Assert.assertEquals(true, fileService.fileExists(fileName));
		fileService.fileDelete(fileName);
	}
}