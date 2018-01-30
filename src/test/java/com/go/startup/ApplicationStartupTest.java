package com.go.startup;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.go.bac.IUserBAC;
import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;

public class ApplicationStartupTest {

	@InjectMocks
	private ApplicationStartup applicationStartup;

	@Mock
	private IUserBAC userBAC;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(applicationStartup, "defaultAdminUsername", "user");
		ReflectionTestUtils.setField(applicationStartup, "defaultAdminPassword", "pass");
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(userBAC);
	}

	@Test
	public void testStartupWhenUserExists() {
		Mockito.when(userBAC.fetchUser("user")).thenReturn(new User());
		applicationStartup.onApplicationEvent(null);
		Mockito.verify(userBAC).fetchUser("user");
	}

	@Test
	public void testStartupWhenUserNotExists() {
		Mockito.when(userBAC.fetchUser("user")).thenThrow(new GoLinkException("user.does.not.exist"));
		applicationStartup.onApplicationEvent(null);
		Mockito.verify(userBAC).fetchUser("user");
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		Mockito.verify(userBAC).createUser(userCaptor.capture());
		Mockito.verify(userBAC).addUserToRole("user", RoleEnum.ADMIN);
		Mockito.verify(userBAC).updateUserActivation("user", true);
		Assert.assertEquals("user", userCaptor.getValue().getUsername());
		Assert.assertEquals("pass", userCaptor.getValue().getPassword());
		Assert.assertNull(userCaptor.getValue().getActive());
		Assert.assertNull(userCaptor.getValue().getRoleList());
	}

	@Test
	public void testStartupUserCreationFailure() {
		Mockito.when(userBAC.fetchUser("user")).thenThrow(new GoLinkException("some.fake.code"));
		try {
			applicationStartup.onApplicationEvent(null);
			Assert.fail();
		} catch (GoLinkException ge) {
			Mockito.verify(userBAC).fetchUser("user");
			Assert.assertEquals("some.fake.code", ge.getErrorMessage().getCode());
		}
	}
}
