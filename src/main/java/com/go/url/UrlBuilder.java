package com.go.url;

import org.springframework.util.StringUtils;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.ParamTypeEnum;

public final class UrlBuilder {

	public static String buildUrl(GoLink link, String param) {
		if (link.getParameter() != null) {
			if (!StringUtils.isEmpty(param)) {
				return buildUrlWithParam(link, param);
			} else if (ParamTypeEnum.PROVIDED.equals(link.getParameter().getType())) {
				throw new GoLinkException("go.link.parameter.required", link.getName());
			}
		}
		return link.getUrl();
	}

	private static String buildUrlWithParam(GoLink link, String param) {
		switch (link.getParameter().getType()) {
		case PATH:
			return buildPathParamUrl(link, param);
		case NAMED:
			return buildNamedParamUrl(link, param);
		case PROVIDED:
			return buildProvidedParamUrl(link, param);
		default:
			return link.getUrl();
		}
	}

	private static String buildPathParamUrl(GoLink link, String param) {
		return link.getUrl() + "/" + param;
	}

	private static String buildNamedParamUrl(GoLink link, String param) {
		return link.getUrl() + "?" + link.getParameter().getName() + "=" + param;
	}

	private static String buildProvidedParamUrl(GoLink link, String param) {
		return link.getUrl().replace("{" + link.getParameter().getName() + "}", param);
	}
}
