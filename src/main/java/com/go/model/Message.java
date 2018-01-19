package com.go.model;

public class Message {
	private String code;
	private Object[] args;

	public Message(String code, Object... args) {
		setCode(code);
		setArgs(args);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
