package com.go.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsageLogTest {

	@Test
	public void testToString() {
		assertEquals("Log [linkName=test, time=123]", createLog("test", 123l).toString());
	}

	private UsageLog createLog(String linkName, Long timeInMillis) {
		UsageLog log = new UsageLog(linkName);
		log.setTime(timeInMillis);
		return log;
	}

}
