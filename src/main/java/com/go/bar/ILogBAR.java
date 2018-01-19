package com.go.bar;

import java.util.List;

import com.go.model.UsageLog;

public interface ILogBAR {

	void log(UsageLog usageLog);

	List<UsageLog> getAllLogs();

}
