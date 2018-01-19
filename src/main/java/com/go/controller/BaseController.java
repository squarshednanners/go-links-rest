package com.go.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

import com.go.exception.GoLinkException;
import com.go.model.Message;
import com.go.model.Response;

public abstract class BaseController {

	@Autowired
	private MessageSourceAccessor messageSourceAccessor;

	@SuppressWarnings("rawtypes")
	protected void formatErrorResponse(Response response, Exception e, Message... messageArr) {
		response.setSuccessful(false);
		formatResponse(response, messageArr);
		if (e instanceof GoLinkException) {
			response.addMessage(getMessage(((GoLinkException) e).getErrorMessage()));
		} else {
			response.addMessage(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	protected void formatSuccessResponse(Response response, Message... messageArr) {
		response.setSuccessful(true);
		formatResponse(response, messageArr);
	}

	@SuppressWarnings("rawtypes")
	private void formatResponse(Response response, Message... messageArr) {
		if (messageArr != null) {
			for (Message message : messageArr) {
				response.addMessage(getMessage(message));
			}
		}
	}

	private String getMessage(Message message) {
		return messageSourceAccessor.getMessage(message.getCode(), message.getArgs());
	}

}
