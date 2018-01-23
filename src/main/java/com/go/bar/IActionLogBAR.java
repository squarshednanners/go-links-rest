package com.go.bar;

import java.util.List;

import com.go.model.ActionLog;

public interface IActionLogBAR {

	void log(ActionLog actionLog);

	List<ActionLog> getAllLogs();

}
