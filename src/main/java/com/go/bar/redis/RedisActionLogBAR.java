package com.go.bar.redis;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IActionLogBAR;
import com.go.model.ActionLog;
import com.go.model.SortDirection;

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
	public List<ActionLog> getAllLogs(SortDirection sortDirection) {
		List<ActionLog> actionLogList = listOps.range(KEY, 0, -1);
		if (SortDirection.DESC.equals(sortDirection)) {
			Collections.reverse(actionLogList);
		}
		return actionLogList;
	}

}
