package com.go.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActionLog extends UsageLog implements Serializable {
	private String username;
	private ActionTypeEnum action;

	public ActionLog() {

	}

	public ActionLog(String username, String linkName, ActionTypeEnum action) {
		super(linkName);
		setUsername(username);
		setAction(action);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ActionTypeEnum getAction() {
		return action;
	}

	public void setAction(ActionTypeEnum action) {
		this.action = action;
	}

}
