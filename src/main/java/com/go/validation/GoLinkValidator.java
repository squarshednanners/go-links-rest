package com.go.validation;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;

@Component
public class GoLinkValidator {

	@Autowired
	private GoLinkParameterValidator goLinkParamValidator;

	public void validateLink(GoLink link) {
		validateName(link.getName());
		validateUrl(link.getUrl());
		goLinkParamValidator.validateParameters(link);
	}

	public void validateName(String name) {
		if (name == null || StringUtils.isEmpty(name)) {
			throw new GoLinkException("go.link.name.required");
		}
	}

	public void validateUrl(String url) {
		if (url == null || StringUtils.isEmpty(url)) {
			throw new GoLinkException("go.link.url.required");
		}
		if (!UrlValidator.getInstance().isValid(pruneUrlForProvidedParams(url))) {
			throw new GoLinkException("go.link.url.invalid", url);
		}
	}

	private String pruneUrlForProvidedParams(String url) {
		return url.replace("{", "").replace("}", "");
	}

}
