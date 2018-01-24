package com.go.bar.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IUsageLogBAR;
import com.go.model.UsageLog;
import com.go.model.SortDirection;

@Repository
public class RedisUsageLogBAR implements IUsageLogBAR {
	private static final String KEY = "usageLog";
	private static final int LOG_PAGE_SIZE = 50;

	@Resource(name = "usageLogRedisTemplate")
	private ListOperations<String, UsageLog> listOps;

	@Override
	public void log(UsageLog log) {
		listOps.rightPush(KEY, log);
	}

	@Override
	public void deleteLog(UsageLog log) {
		listOps.remove(KEY, 1, log);
	}

	@Override
	public List<UsageLog> fetchPagedLogsSortedAscending(Integer pageNumber, Integer pageSize) {
		Integer startIndex = getPageIndex(pageNumber, pageSize);
		Integer endIndex = startIndex + pageSize - 1;
		return listOps.range(KEY, startIndex, endIndex);
	}

	@Override
	public List<UsageLog> fetchPagedLogsSortedDescending(Integer pageNumber, Integer pageSize) {
		Long totalSizeIndex = listOps.size(KEY) - 1;
		Long startIndex = totalSizeIndex - ((pageNumber * pageSize) - 1);
		Long endIndex = totalSizeIndex - getPageIndex(pageNumber, pageSize);
		return listOps.range(KEY, startIndex, endIndex);
	}

	@Override
	public List<UsageLog> fetchUsageLogsByInterval(Long startTimeInMillis, Long endTimeInMillis,
			SortDirection sortDirection) {
		List<UsageLog> matchingUsageLogs = new ArrayList<UsageLog>();
		boolean hasMoreLogs = true;
		int pageNum = 1;
		while (hasMoreLogs) {
			List<UsageLog> tempUsageLogs;
			if (isSortAsc(sortDirection)) {
				tempUsageLogs = fetchPagedLogsSortedAscending(pageNum++, LOG_PAGE_SIZE);
			} else {
				tempUsageLogs = fetchPagedLogsSortedDescending(pageNum++, LOG_PAGE_SIZE);
			}
			if (tempUsageLogs.isEmpty()) {
				hasMoreLogs = false;
			}
			for (UsageLog log : tempUsageLogs) {
				if (startTimeInMillis <= log.getTime() && endTimeInMillis >= log.getTime()) {
					matchingUsageLogs.add(log);
					// Only quit if:
					// sorting asc & log time is greater (newer) than end time
					// sorting desc & log time is less (older) than start time
				} else if ((isSortAsc(sortDirection) && log.getTime() > endTimeInMillis)
						|| (!isSortAsc(sortDirection) && log.getTime() < startTimeInMillis)) {
					hasMoreLogs = false;
				}
			}
		}
		return matchingUsageLogs;
	}

	private boolean isSortAsc(SortDirection sortDirection) {
		return SortDirection.ASC.equals(sortDirection);
	}

	private int getPageIndex(Integer pageNumber, Integer pageSize) {
		return (pageNumber - 1) * pageSize;
	}

}
