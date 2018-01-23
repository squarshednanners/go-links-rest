package com.go.bar.redis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IActionLogBAR;
import com.go.model.ActionLog;

@Repository
public class RedisActionLogBAR implements IActionLogBAR {

	private static final String KEY = "actionLog";

	@Resource(name = "actionLogRedisTemplate")
	private ListOperations<String, ActionLog> listOps;

	@Override
	public void log(ActionLog actionLog) {
		listOps.rightPush(KEY, actionLog);
	}

	@Override
	public List<ActionLog> getAllLogs() {
		return listOps.range(KEY, 0, -1);
	}

}
