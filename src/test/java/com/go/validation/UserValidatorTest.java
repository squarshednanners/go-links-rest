package com.go.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;
import com.go.validation.UserValidator;

public class UserValidatorTest {

	private UserValidator validator;

	@Before
	public void setUp() {
		validator = new UserValidator();
	}

	@Test
	public void testValidationSuccess() {
		validator.validateUser(createUser("test@test.com", "password"));
	}

	@Test
	public void testNullUsername() {
		try {
			validator.validateUser(createUser(null, "password"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.name.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyUsername() {
		try {
			validator.validateUser(createUser("", "password"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.name.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testInvalidUsername() {
		try {
			validator.validateUser(createUser("test.com", "password"));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.name.invalid", e.getErrorMessage().getCode());
			assertEquals(1, e.getErrorMessage().getArgs().length);
			assertEquals("test.com", e.getErrorMessage().getArgs()[0]);
		}
	}

	@Test
	public void testNullPassword() {
		try {
			validator.validateUser(createUser("test@test.com", null));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.password.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyPassword() {
		try {
			validator.validateUser(createUser("test@test.com", ""));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.password.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyActiveFlag() {
		try {
			validator.validateUser(createUser("test@test.com", "password", null, RoleEnum.USER));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.activation.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testEmptyRole() {
		try {
			validator.validateUser(createUser("test@test.com", "password", true, null));
			fail();
		} catch (GoLinkException e) {
			assertEquals("user.role.required", e.getErrorMessage().getCode());
			assertEquals(0, e.getErrorMessage().getArgs().length);
		}
	}

	private User createUser(String username, String password) {
		return createUser(username, password, true, RoleEnum.USER);
	}

	private User createUser(String username, String password, Boolean active, RoleEnum role) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setActive(active);
		if (role != null) {
			user.addRole(role);
		}
		return user;
	}

}
