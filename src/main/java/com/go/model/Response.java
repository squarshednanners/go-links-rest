package com.go.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Response<T> implements Serializable {

	private T results;
	private Boolean successful;
	private List<String> messageList = new ArrayList<>();

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}

	public Boolean getSuccessful() {
		return successful;
	}

	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public void addMessage(String message) {
		this.messageList.add(message);
	}

}
