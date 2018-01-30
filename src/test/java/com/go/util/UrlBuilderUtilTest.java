package com.go.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.GoLinkParameter;
import com.go.model.ParamTypeEnum;
import com.go.util.UrlBuilderUtil;

public class UrlBuilderUtilTest {

	@Test
	public void testBuildUrlWithNoParam() {
		assertEquals("http://www.test.com", UrlBuilderUtil.buildUrl(createLink("http://www.test.com"), null));
	}

	@Test
	public void testBuildUrlWithPathParam() {
		assertEquals("http://www.test.com/testParam", UrlBuilderUtil
				.buildUrl(createLink("http://www.test.com", createParam(null, ParamTypeEnum.PATH)), "testParam"));
	}

	@Test
	public void testBuildUrlWithNamedParam() {
		assertEquals("http://www.test.com?param1Name=testParam", UrlBuilderUtil.buildUrl(
				createLink("http://www.test.com", createParam("param1Name", ParamTypeEnum.NAMED)), "testParam"));
	}

	@Test
	public void testBuildUrlWithProvidedParam() {
		assertEquals("http://www.test.com?param1Name=testParam", UrlBuilderUtil.buildUrl(
				createLink("http://www.test.com?param1Name={param1}", createParam("param1", ParamTypeEnum.PROVIDED)),
				"testParam"));
	}

	@Test
	public void testBuildUrlForProvidedParamWithNoParam() {
		try {
			UrlBuilderUtil.buildUrl(createLink("http://www.test.com?param1Name={param1}",
					createParam("param1", ParamTypeEnum.PROVIDED)), "");
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.parameter.required", ge.getErrorMessage().getCode());
			assertEquals(1, ge.getErrorMessage().getArgs().length);
			assertEquals("testLink", ge.getErrorMessage().getArgs()[0]);
		}
	}

	@Test
	public void testBuildUrlWithPathButNoPathConfig() {
		assertEquals("http://www.test.com", UrlBuilderUtil.buildUrl(createLink("http://www.test.com"), "test"));
	}

	private GoLink createLink(String url) {
		return createLink(url, null);
	}

	private GoLink createLink(String url, GoLinkParameter parameter) {
		GoLink link = new GoLink();
		link.setName("testLink");
		link.setUrl(url);
		link.setParameter(parameter);
		return link;
	}

	private GoLinkParameter createParam(String paramName, ParamTypeEnum paramType) {
		GoLinkParameter param = new GoLinkParameter();
		param.setName(paramName);
		param.setType(paramType);
		return param;
	}

}
