package com.go.bac;

import java.util.List;

import com.go.model.UsageLog;

public interface ILogBAC {

	void logLinkCreation(String username, String linkName);

	void logLinkDeletion(String username, String linkName);

	List<UsageLog> fetchAllLogs();

	List<UsageLog> fetchLogsForUser(String username);
}
