package com.go.exception;

import com.go.model.Message;

@SuppressWarnings("serial")
public class GoLinkException extends RuntimeException {

	private Message errorMessage;

	public GoLinkException(String errorCode, Object... args) {
		setErrorMessage(new Message(errorCode, args));
	}

	public Message getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Message errorMessage) {
		this.errorMessage = errorMessage;
	}

}
