package com.go.bac.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageLogBAC;
import com.go.bar.IUsageLogBAR;
import com.go.model.UsageLog;
import com.go.model.SortDirection;

@Component
public class UsageLogBACImpl implements IUsageLogBAC {

	final static Logger LOGGER = LoggerFactory.getLogger(UsageLogBACImpl.class);

	@Autowired
	private IUsageLogBAR usageLogBAR;

	@Override
	public void logUsage(String linkName) {
		usageLogBAR.log(new UsageLog(linkName));
	}

	@Override
	public void deleteUsageLogs(Integer daysToKeep) {
		List<UsageLog> usageLogs = usageLogBAR.fetchUsageLogsByInterval(0l,
				midnightToday().minusDays(daysToKeep).toInstant().toEpochMilli(), SortDirection.ASC);
		for (UsageLog log : usageLogs) {
			LOGGER.debug("Deleting Usage Log: " + log.toString());
			usageLogBAR.deleteLog(log);
		}
		LOGGER.info("Deleted " + usageLogs.size() + " Usage Logs");
	}

	private ZonedDateTime midnightToday() {
		return ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

}
