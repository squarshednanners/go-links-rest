package com.go.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UsageLog implements Serializable {
	private String username;
	private String linkName;
	private Long time;
	private ActionTypeEnum action;

	public UsageLog() {

	}

	public UsageLog(String username, String linkName, ActionTypeEnum action) {
		setLinkName(linkName);
		setTime(System.currentTimeMillis());
		setUsername(username);
		setAction(action);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public ActionTypeEnum getAction() {
		return action;
	}

	public void setAction(ActionTypeEnum action) {
		this.action = action;
	}

}
