package com.go.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GoLink implements Serializable {
	private String name;
	private String url;
	private GoLinkParameter parameter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GoLinkParameter getParameter() {
		return parameter;
	}

	public void setParameter(GoLinkParameter parameter) {
		this.parameter = parameter;
	}

}
