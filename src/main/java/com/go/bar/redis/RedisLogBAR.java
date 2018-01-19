package com.go.bar.redis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.ILogBAR;
import com.go.model.UsageLog;

@Repository
public class RedisLogBAR implements ILogBAR {

	private static final String KEY = "log";

	@Resource(name = "usageLogRedisTemplate")
	private ListOperations<String, UsageLog> listOps;

	@Override
	public void log(UsageLog usageLog) {
		listOps.rightPush(KEY, usageLog);
	}

	@Override
	public List<UsageLog> getAllLogs() {
		return listOps.range(KEY, 0, -1);
	}

}
