package com.go.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;

@Component
public class GoLinkParameterValidator {

	public void validateParameters(GoLink link) {
		if (link.getParameter() != null) {
			switch (link.getParameter().getType()) {
			case PATH:
				// No further validation needed
				break;
			case NAMED:
				validateNamedParam(link);
				break;
			case PROVIDED:
				validateProvidedParam(link);
				break;
			default:
				throw new RuntimeException("Unable to determine parameter type");
			}
		}
	}

	private void validateNamedParam(GoLink link) {
		if (StringUtils.isEmpty(link.getParameter().getName())) {
			throw new GoLinkException("go.link.parameter.name.required");
		}
	}

	private void validateProvidedParam(GoLink link) {
		validateNamedParam(link);
		if (!link.getUrl().contains("{" + link.getParameter().getName() + "}")) {
			throw new GoLinkException("go.link.parameter.name.url.mismatch", link.getParameter().getName(),
					link.getUrl());
		}
	}
}
