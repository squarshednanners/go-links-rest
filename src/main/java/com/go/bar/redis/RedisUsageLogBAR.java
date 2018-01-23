package com.go.bar.redis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IUsageLogBAR;
import com.go.model.Log;

@Repository
public class RedisUsageLogBAR implements IUsageLogBAR {
	private static final String KEY = "usageLog";

	@Resource(name = "usageLogRedisTemplate")
	private ListOperations<String, Log> listOps;

	@Override
	public void log(Log log) {
		listOps.rightPush(KEY, log);
	}

	@Override
	public List<Log> getOldestUsageLogs(Integer pageNumber, Integer pageSize) {
		Integer startIndex = getPageIndex(pageNumber, pageSize);
		Integer endIndex = startIndex + pageSize - 1;
		return listOps.range(KEY, startIndex, endIndex);
	}

	@Override
	public List<Log> getNewestUsageLogs(Integer pageNumber, Integer pageSize) {
		Long totalSizeIndex = listOps.size(KEY) - 1;
		Long startIndex = totalSizeIndex - ((pageNumber * pageSize) - 1);
		Long endIndex = totalSizeIndex - getPageIndex(pageNumber, pageSize);
		return listOps.range(KEY, startIndex, endIndex);
	}

	@Override
	public void deleteLog(Log log) {
		listOps.remove(KEY, 1, log);
	}

	private int getPageIndex(Integer pageNumber, Integer pageSize) {
		return (pageNumber - 1) * pageSize;
	}

}
