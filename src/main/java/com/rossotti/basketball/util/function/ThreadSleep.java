package com.rossotti.basketball.util.function;

public class ThreadSleep {
	public static void sleep(int nbrSeconds) {
		try {
			Thread.sleep(nbrSeconds * 1000);
		}
		catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}