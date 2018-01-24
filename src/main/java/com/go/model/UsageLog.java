package com.go.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UsageLog implements Serializable {

	private String linkName;
	private Long time;

	public UsageLog() {

	}

	public UsageLog(String linkName) {
		setLinkName(linkName);
		setTime(System.currentTimeMillis());
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

	@Override
	public String toString() {
		return "Log [linkName=" + linkName + ", time=" + time + "]";
	}

}
