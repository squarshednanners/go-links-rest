package com.go.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.GoLinkParameter;
import com.go.model.ParamTypeEnum;
import com.go.validation.GoLinkParameterValidator;

public class GoLinkParameterValidatorTest {
	private GoLinkParameterValidator validator;

	@Before
	public void setUp() {
		validator = new GoLinkParameterValidator();
	}

	@Test
	public void testNoParameterSuccess() {
		validator.validateParameters(createLink("name", "http://go.com", null));
	}

	@Test
	public void testPathParameterSuccess() {
		validator.validateParameters(createLink("name", "http://go.com", createParam(ParamTypeEnum.PATH, null)));
	}

	@Test
	public void testNamedParameterSuccess() {
		validator.validateParameters(createLink("name", "http://go.com", createParam(ParamTypeEnum.NAMED, "stuff")));
	}

	@Test
	public void testNamedParameterErrorNoName() {
		try {
			validator.validateParameters(createLink("name", "http://go.com", createParam(ParamTypeEnum.NAMED, null)));
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.parameter.name.required", ge.getErrorMessage().getCode());
			assertEquals(0, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testProvidedParameterSuccess() {
		validator.validateParameters(
				createLink("name", "http://go.com/{stuff}/moreurl", createParam(ParamTypeEnum.PROVIDED, "stuff")));
	}

	@Test
	public void testProvidedParameterErrorNoName() {
		try {
			validator.validateParameters(
					createLink("name", "http://go.com/{stuff}/moreurl", createParam(ParamTypeEnum.PROVIDED, "")));
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.parameter.name.required", ge.getErrorMessage().getCode());
			assertEquals(0, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testProvidedParameterErrorUrlMismatch() {
		try {
			validator.validateParameters(createLink("name", "http://go.com/{stuff}/moreurl",
					createParam(ParamTypeEnum.PROVIDED, "randomParamName")));
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.parameter.name.url.mismatch", ge.getErrorMessage().getCode());
			assertEquals(2, ge.getErrorMessage().getArgs().length);
			assertEquals("randomParamName", ge.getErrorMessage().getArgs()[0]);
			assertEquals("http://go.com/{stuff}/moreurl", ge.getErrorMessage().getArgs()[1]);
		}
	}

	private GoLink createLink(String name, String url, GoLinkParameter param) {
		GoLink link = new GoLink();
		link.setName(name);
		link.setUrl(url);
		link.setParameter(param);
		return link;
	}

	private GoLinkParameter createParam(ParamTypeEnum type, String name) {
		GoLinkParameter param = new GoLinkParameter();
		param.setName(name);
		param.setType(type);
		return param;
	}

}
