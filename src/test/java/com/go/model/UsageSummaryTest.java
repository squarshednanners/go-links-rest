package com.go.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsageSummaryTest {

	@Test
	public void testToString() {
		assertEquals("UsageSummary [startTime=123, totalCount=50]", createUsageSummary(50, 123l).toString());
	}

	private UsageSummary createUsageSummary(Integer totalCount, Long timeInMillis) {
		UsageSummary log = new UsageSummary();
		log.setStartTime(timeInMillis);
		log.setTotalCount(totalCount);
		return log;
	}

}
