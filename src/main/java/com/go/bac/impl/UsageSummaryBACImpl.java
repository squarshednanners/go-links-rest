package com.go.bac.impl;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageSummaryBAC;
import com.go.bar.IUsageLogBAR;
import com.go.bar.IUsageSummaryBAR;
import com.go.model.SortDirection;
import com.go.model.UsageLog;
import com.go.model.UsageSummary;
import com.go.util.TimeUtil;

@Component
public class UsageSummaryBACImpl implements IUsageSummaryBAC {

	final static Logger LOGGER = LoggerFactory.getLogger(UsageLogBACImpl.class);

	@Autowired
	private IUsageLogBAR usageLogBAR;

	@Autowired
	private IUsageSummaryBAR usageSummaryBAR;

	@Override
	public List<UsageSummary> fetchAllHourlySummaries() {
		return usageSummaryBAR.fetchAllHourlySummaries();
	}

	@Override
	public List<UsageSummary> fetchAllDailySummaries() {
		return usageSummaryBAR.fetchAllDailySummaries();
	}

	@Override
	public UsageSummary fetchTotalSummary() {
		return usageSummaryBAR.fetchTotalSummary();
	}

	@Override
	public void deleteUsageSummary(Integer usageSummaryDaysToKeep) {
		Long deleteThreshold = TimeUtil.topOfDay().minusDays(usageSummaryDaysToKeep).toInstant().toEpochMilli();
		deleteHourlyUsageSummary(deleteThreshold);
		deleteDailyUsageSummary(deleteThreshold);
	}

	@Override
	public void calculateHourlySummary() {
		Long hourStart = hourSummaryBegin();
		Long hourEnd = hourSummaryEnd();
		List<UsageLog> hourlyUsageLogs = usageLogBAR.fetchUsageLogsByInterval(hourStart, hourEnd, SortDirection.DESC);
		UsageSummary summary = createSummary(hourStart, hourlyUsageLogs);
		usageSummaryBAR.addHourlySummary(summary);
	}

	@Override
	public void calculateDailySummary() {
		Long dayStart = daySummaryBegin();
		Long dayEnd = daySummaryEnd();
		List<UsageLog> dailyUsageLogs = usageLogBAR.fetchUsageLogsByInterval(dayStart, dayEnd, SortDirection.DESC);
		UsageSummary summary = createSummary(dayStart, dailyUsageLogs);
		usageSummaryBAR.addDailySummary(summary);
		aggregateTotalSummary();
	}

	private void deleteHourlyUsageSummary(Long deleteThreshold) {
		boolean hasMoreLogs = true;
		int pageNum = 1;
		while (hasMoreLogs) {
			List<UsageSummary> tempUsageSummary = usageSummaryBAR.fetchPagedHourlySummary(pageNum++, 25);
			if (tempUsageSummary.isEmpty()) {
				hasMoreLogs = false;
			}
			for (UsageSummary usageSummary : tempUsageSummary) {
				if (usageSummary.getStartTime() < deleteThreshold) {
					LOGGER.debug("Deleting Hourly Usage Summary: " + usageSummary.toString());
					usageSummaryBAR.deleteHourlySummary(usageSummary);
				} else {
					hasMoreLogs = false;
					break;
				}
			}
		}
	}

	private void deleteDailyUsageSummary(Long deleteThreshold) {
		boolean hasMoreLogs = true;
		int pageNum = 1;
		while (hasMoreLogs) {
			List<UsageSummary> tempUsageSummary = usageSummaryBAR.fetchPagedDailySummary(pageNum++, 2);
			if (tempUsageSummary.isEmpty()) {
				hasMoreLogs = false;
			}
			for (UsageSummary usageSummary : tempUsageSummary) {
				if (usageSummary.getStartTime() < deleteThreshold) {
					LOGGER.debug("Deleting Daily Usage Summary: " + usageSummary.toString());
					usageSummaryBAR.deleteDailySummary(usageSummary);
				} else {
					hasMoreLogs = false;
					break;
				}
			}
		}
	}

	private UsageSummary createSummary(Long startTime, List<UsageLog> usageLogs) {
		UsageSummary summary = new UsageSummary();
		summary.setStartTime(startTime);
		for (UsageLog log : usageLogs) {
			summary.addLinkCount(log.getLinkName());
		}
		return summary;
	}

	private void aggregateTotalSummary() {
		UsageSummary totalSummary = new UsageSummary();
		Long oldestDailyRecord = Long.MAX_VALUE;
		List<UsageSummary> dailySummaries = usageSummaryBAR.fetchAllDailySummaries();
		for (UsageSummary dailySummary : dailySummaries) {
			if (dailySummary.getStartTime() < oldestDailyRecord) {
				oldestDailyRecord = dailySummary.getStartTime();
			}
			for (Entry<String, Integer> dailyEntry : dailySummary.getLinkCountMap().entrySet()) {
				totalSummary.addLinkCount(dailyEntry.getKey(), dailyEntry.getValue());
			}
		}
		totalSummary.setStartTime(oldestDailyRecord);
		usageSummaryBAR.setTotalSummary(totalSummary);
	}

	private long hourSummaryBegin() {
		return TimeUtil.topOfHour().minusHours(1).toInstant().toEpochMilli();
	}

	private long hourSummaryEnd() {
		return TimeUtil.topOfHour().minusNanos(1).toInstant().toEpochMilli();
	}

	private long daySummaryBegin() {
		return TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
	}

	private long daySummaryEnd() {
		return TimeUtil.topOfDay().minusNanos(1).toInstant().toEpochMilli();
	}

}
