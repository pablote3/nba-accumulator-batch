package com.rossotti.basketball.util.function;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamConverter {
	static public byte[] getBytes(InputStream inputStream) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		try {
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
		}
		catch (IOException ioe) {
			return null;
		}
		return buffer.toByteArray();
	}
}