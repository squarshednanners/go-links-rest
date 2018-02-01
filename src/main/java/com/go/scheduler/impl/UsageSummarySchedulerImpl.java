package com.go.scheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageSummaryBAC;
import com.go.scheduler.IUsageSummaryScheduler;

@Component
public class UsageSummarySchedulerImpl implements IUsageSummaryScheduler {

	final static Logger LOGGER = LoggerFactory.getLogger(UsageSummarySchedulerImpl.class);

	@Autowired
	private IUsageSummaryBAC usageSummaryBAC;

	@Value("${retention.houurly.usage.summary.days.to.keep}")
	private Integer hourlyUsageSummaryDaysToKeep;

	@Value("${retention.daily.usage.summary.days.to.keep}")
	private Integer dailyUsageSummaryDaysToKeep;

	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteUsageSummary() {
		deleteHourlyUsageSummary();
		deleteDailyUsageSummary();
	}

	@Override
	@Scheduled(cron = "0 5 * * * ?")
	public void calculateHourlySummary() {
		LOGGER.info("Starting Hourly Usage Summary Calculation");
		usageSummaryBAC.calculateHourlySummary();
		LOGGER.info("Completed Hourly Usage Summary Calculation");
	}

	@Override
	@Scheduled(cron = "0 10 0 * * ?")
	public void calculateDailySummary() {
		LOGGER.info("Starting Daily Usage Summary Calculation");
		usageSummaryBAC.calculateDailySummary();
		LOGGER.info("Completed Daily Usage Summary Calculation");
	}

	private void deleteHourlyUsageSummary() {
		LOGGER.info("Starting Scheduled Delete of Hourly Usage Summary. Deleting summaries older than "
				+ hourlyUsageSummaryDaysToKeep + " days");
		usageSummaryBAC.deleteHourlyUsageSummary(hourlyUsageSummaryDaysToKeep);
		LOGGER.info("Completed Scheduled Delete of Hourly Usage Summary");
	}

	private void deleteDailyUsageSummary() {
		LOGGER.info("Starting Scheduled Delete of Daily Usage Summary. Deleting summaries older than "
				+ dailyUsageSummaryDaysToKeep + " days");
		usageSummaryBAC.deleteDailyUsageSummary(dailyUsageSummaryDaysToKeep);
		LOGGER.info("Completed Scheduled Delete of Daily Usage Summary");
	}

}
