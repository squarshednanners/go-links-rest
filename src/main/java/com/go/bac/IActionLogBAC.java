package com.go.bac;

import java.util.List;

import com.go.model.ActionLog;

public interface IActionLogBAC {

	void logLinkCreation(String username, String linkName);

	void logLinkDeletion(String username, String linkName);

	List<ActionLog> fetchAllLogs();

	List<ActionLog> fetchLogsForUser(String username);
}
