package com.go.bar.redis;

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

}
