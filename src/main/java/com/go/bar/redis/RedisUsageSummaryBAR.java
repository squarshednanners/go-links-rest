package com.go.bar.redis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IUsageSummaryBAR;
import com.go.model.UsageSummary;

@Repository
public class RedisUsageSummaryBAR implements IUsageSummaryBAR {
	private static final String TOTAL_SUMMARY_KEY = "totalUsageSummary";
	private static final String HOURLY_SUMMARY_KEY = "hourlyUsageSummary";
	private static final String DAILY_SUMMARY_KEY = "dailyUsageSummary";

	@Resource(name = "usageSummaryRedisTemplate")
	private ListOperations<String, UsageSummary> listOps;

	@Resource(name = "usageSummaryRedisTemplate")
	private ValueOperations<String, UsageSummary> valueOps;

	@Override
	public List<UsageSummary> fetchAllHourlySummaries() {
		return listOps.range(HOURLY_SUMMARY_KEY, 0, -1);
	}

	@Override
	public List<UsageSummary> fetchPagedHourlySummary(Integer pageNumber, Integer pageSize) {
		Integer startIndex = getStartIndex(pageNumber, pageSize);
		Integer endIndex = getEndIndex(startIndex, pageSize);
		return listOps.range(HOURLY_SUMMARY_KEY, startIndex, endIndex);
	}

	@Override
	public void addHourlySummary(UsageSummary summary) {
		listOps.rightPush(HOURLY_SUMMARY_KEY, summary);
	}

	@Override
	public void deleteHourlySummary(UsageSummary summary) {
		listOps.remove(HOURLY_SUMMARY_KEY, 1, summary);
	}

	@Override
	public List<UsageSummary> fetchAllDailySummaries() {
		return listOps.range(DAILY_SUMMARY_KEY, 0, -1);
	}

	@Override
	public List<UsageSummary> fetchPagedDailySummary(Integer pageNumber, Integer pageSize) {
		Integer startIndex = getStartIndex(pageNumber, pageSize);
		Integer endIndex = getEndIndex(startIndex, pageSize);
		return listOps.range(DAILY_SUMMARY_KEY, startIndex, endIndex);
	}

	@Override
	public void addDailySummary(UsageSummary summary) {
		listOps.rightPush(DAILY_SUMMARY_KEY, summary);
	}

	@Override
	public void deleteDailySummary(UsageSummary summary) {
		listOps.remove(DAILY_SUMMARY_KEY, 1, summary);
	}

	@Override
	public UsageSummary fetchTotalSummary() {
		return valueOps.get(TOTAL_SUMMARY_KEY);
	}

	@Override
	public void setTotalSummary(UsageSummary summary) {
		valueOps.set(TOTAL_SUMMARY_KEY, summary);
	}

	private int getStartIndex(Integer pageNumber, Integer pageSize) {
		return (pageNumber - 1) * pageSize;
	}

	private int getEndIndex(Integer startIndex, Integer pageSize) {
		return startIndex + pageSize - 1;
	}

}
