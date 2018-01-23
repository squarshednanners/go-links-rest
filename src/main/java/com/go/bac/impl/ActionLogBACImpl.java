package com.go.bac.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IActionLogBAC;
import com.go.bar.IActionLogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.ActionLog;

@Component
public class ActionLogBACImpl implements IActionLogBAC {

	@Autowired
	private IActionLogBAR actionLogBAR;

	@Override
	public void logLinkCreation(String username, String linkName) {
		actionLogBAR.log(new ActionLog(username, linkName, ActionTypeEnum.CREATE));
	}

	@Override
	public void logLinkDeletion(String username, String linkName) {
		actionLogBAR.log(new ActionLog(username, linkName, ActionTypeEnum.DELETE));
	}

	@Override
	public List<ActionLog> fetchAllLogs() {
		return actionLogBAR.getAllLogs();
	}

	@Override
	public List<ActionLog> fetchLogsForUser(String username) {
		List<ActionLog> userLogs = new ArrayList<ActionLog>();
		for (ActionLog log : actionLogBAR.getAllLogs()) {
			if (username.equalsIgnoreCase(log.getUsername())) {
				userLogs.add(log);
			}
		}
		return userLogs;
	}

}
