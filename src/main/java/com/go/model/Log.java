package com.go.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Log implements Serializable {

	private String linkName;
	private Long time;

	public Log() {

	}

	public Log(String linkName) {
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

}
