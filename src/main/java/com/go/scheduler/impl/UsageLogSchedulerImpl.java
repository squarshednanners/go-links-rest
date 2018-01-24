package com.go.scheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageLogBAC;
import com.go.scheduler.IUsageLogScheduler;

@Component
public class UsageLogSchedulerImpl implements IUsageLogScheduler {

	final static Logger LOGGER = LoggerFactory.getLogger(UsageLogSchedulerImpl.class);

	@Autowired
	private IUsageLogBAC usageLogBAC;

	@Value("${retention.usage.log.days.to.keep}")
	private Integer usageLogDaysToKeep;

	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteUsageLogs() {
		LOGGER.info(
				"Starting Scheduled Delete of Usage Logs. Deleting logs older than " + usageLogDaysToKeep + " days");
		usageLogBAC.deleteUsageLogs(usageLogDaysToKeep);
		LOGGER.info("Completed Scheduled Delete of Usage Logs");
	}

}
