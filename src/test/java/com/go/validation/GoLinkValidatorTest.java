package com.go.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.validation.GoLinkParameterValidator;
import com.go.validation.GoLinkValidator;

public class GoLinkValidatorTest {

	@InjectMocks
	private GoLinkValidator validator;

	@Mock
	private GoLinkParameterValidator parameterValidator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(parameterValidator);
	}

	@Test
	public void testValidationSuccess() {
		GoLink goLink = createGoLink("test", "http://google.com");
		validator.validateLink(goLink);
		Mockito.verify(parameterValidator).validateParameters(goLink);
	}

	@Test
	public void testNullLinkName() {
		try {
			validator.validateLink(createGoLink(null, "password"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("go.link.name.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyLinkName() {
		try {
			validator.validateLink(createGoLink("", "password"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("go.link.name.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testNullUrl() {
		try {
			validator.validateLink(createGoLink("test@test.com", null));
			fail();
		} catch (GoLinkException e) {
			assertEquals("go.link.url.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyUrl() {
		try {
			validator.validateLink(createGoLink("test@test.com", ""));
			fail();
		} catch (GoLinkException e) {
			assertEquals("go.link.url.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testInvalidUrl() {
		try {
			validator.validateLink(createGoLink("test.com", "google.com"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("go.link.url.invalid", e.getErrorMessage().getCode());
			assertEquals(1, e.getErrorMessage().getArgs().length);
			assertEquals("google.com", e.getErrorMessage().getArgs()[0]);
		}
	}

	@Test
	public void testValidPrunedUrl() {
		GoLink goLink = createGoLink("test", "http://google.com/{pruned}");
		validator.validateLink(goLink);
		Mockito.verify(parameterValidator).validateParameters(goLink);
	}

	private GoLink createGoLink(String name, String url) {
		GoLink goLink = new GoLink();
		goLink.setName(name);
		goLink.setUrl(url);
		return goLink;
	}

}
