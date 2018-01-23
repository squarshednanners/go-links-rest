package com.go.bac.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IUsageLogBAC;
import com.go.bar.IUsageLogBAR;
import com.go.model.Log;

@Component
public class UsageLogBACImpl implements IUsageLogBAC {

	@Autowired
	private IUsageLogBAR usageLogBAR;

	@Override
	public void logUsage(String linkName) {
		usageLogBAR.log(new Log(linkName));
	}

}
