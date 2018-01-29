package com.go.bar;

import java.util.List;

import com.go.model.ActionLog;
import com.go.model.SortDirection;

public interface IActionLogBAR {

	void log(ActionLog actionLog);

	List<ActionLog> getAllLogs(SortDirection sortDirection);

}
