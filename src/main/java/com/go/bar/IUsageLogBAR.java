package com.go.bar;

import java.util.List;

import com.go.model.Log;

public interface IUsageLogBAR {
	void log(Log log);

	List<Log> getOldestUsageLogs(Integer pageNumber, Integer pageSize);

	List<Log> getNewestUsageLogs(Integer pageNumber, Integer pageSize);

	void deleteLog(Log log);
}
