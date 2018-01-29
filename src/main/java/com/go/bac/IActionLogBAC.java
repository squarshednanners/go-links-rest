package com.go.bac;

import java.util.List;

import com.go.model.ActionLog;
import com.go.model.SortDirection;

public interface IActionLogBAC {

	void logLinkCreation(String username, String linkName);

	void logLinkDeletion(String username, String linkName);

	List<ActionLog> fetchAllLogs(SortDirection sortDirection);

	List<ActionLog> fetchLogsForUser(String username);
}
