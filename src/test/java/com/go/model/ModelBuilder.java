package com.go.model;

public class ModelBuilder {

	public static final UsageLog createLog(long timeInMillis) {
		return createLog("test", timeInMillis);
	}

	public static final UsageLog createLog(String linkName, Long timeInMillis) {
		UsageLog log = new UsageLog(linkName);
		log.setTime(timeInMillis);
		return log;
	}

	public static final UsageSummary createSummary(long startTime, String... linkNames) {
		UsageSummary summary = new UsageSummary();
		summary.setStartTime(startTime);
		for (String linkName : linkNames) {
			summary.addLinkCount(linkName);
		}
		return summary;
	}
}
