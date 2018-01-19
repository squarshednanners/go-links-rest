package com.go.bac.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.ILogBAC;
import com.go.bar.ILogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.UsageLog;

@Component
public class LogBACImpl implements ILogBAC {

	@Autowired
	private ILogBAR usageLogBAR;

	@Override
	public void logLinkCreation(String username, String linkName) {
		usageLogBAR.log(new UsageLog(username, linkName, ActionTypeEnum.CREATE));
	}

	@Override
	public void logLinkDeletion(String username, String linkName) {
		usageLogBAR.log(new UsageLog(username, linkName, ActionTypeEnum.DELETE));
	}

	@Override
	public List<UsageLog> fetchAllLogs() {
		return usageLogBAR.getAllLogs();
	}

	@Override
	public List<UsageLog> fetchLogsForUser(String username) {
		List<UsageLog> userLogs = new ArrayList<UsageLog>();
		for (UsageLog log : usageLogBAR.getAllLogs()) {
			if (username.equalsIgnoreCase(log.getUsername())) {
				userLogs.add(log);
			}
		}
		return userLogs;
	}

}
