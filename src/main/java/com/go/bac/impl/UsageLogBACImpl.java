package com.go.bac.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageLogBAC;
import com.go.bar.IUsageLogBAR;
import com.go.model.Log;

@Component
public class UsageLogBACImpl implements IUsageLogBAC {

	private static final long MILLIS_IN_DAY = 86400000;

	final static Logger LOGGER = LoggerFactory.getLogger(UsageLogBACImpl.class);

	@Autowired
	private IUsageLogBAR usageLogBAR;

	@Override
	public void logUsage(String linkName) {
		usageLogBAR.log(new Log(linkName));
	}

	@Override
	public void deleteUsageLogs(Integer daysToKeep) {
		List<Log> usageLogs;
		Long currentTime = System.currentTimeMillis();
		Long deleteThresholdTime = currentTime - (daysToKeep * MILLIS_IN_DAY);
		Boolean hasMoreLogs = true;
		int deleteCount = 0;
		do {
			usageLogs = usageLogBAR.getOldestUsageLogs(1, 100);
			for (Log log : usageLogs) {
				if (isOlderThanThreshold(log, deleteThresholdTime)) {
					LOGGER.debug("Deleting Usage Log: " + log.toString());
					usageLogBAR.deleteLog(log);
					deleteCount++;
				} else {
					hasMoreLogs = false;
					break;
				}
			}
		} while (!usageLogs.isEmpty() && hasMoreLogs);
		LOGGER.info("Deleted " + deleteCount + " Usage Logs");
	}

	boolean isOlderThanThreshold(Log log, Long threshold) {
		return log.getTime() < threshold;
	}

}
