package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.go.bac.impl.UserBACImpl;
import com.go.bar.IUserBAR;
import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;
import com.go.validation.UserValidator;

public class UserBACImplTest {
	@InjectMocks
	private UserBACImpl userBAC;

	@Mock
	private IUserBAR userBAR;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserValidator userValidator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(userValidator);
		verifyNoMoreInteractions(passwordEncoder);
		verifyNoMoreInteractions(userBAR);
	}

	@Test
	public void testGetAllUsers() {
		List<User> userList = new ArrayList<>();
		when(userBAR.getAllUsers()).thenReturn(userList);
		List<User> actualUserList = userBAC.fetchAllUsers();
		assertEquals(userList, actualUserList);
		verify(userBAR).getAllUsers();
	}

	@Test
	public void testGetUser() {
		User expectedUser = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(expectedUser);
		User actualUser = userBAC.fetchUser("TEST@TEST.COM");
		assertEquals(expectedUser, actualUser);
		verify(userValidator).validateUsername("TEST@TEST.COM");
		verify(userBAR).getUser("test@test.com");
	}

	@Test
	public void testGetUserWhenNotExists() {
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		try {
			userBAC.fetchUser("TEST@TEST.COM");
		} catch (GoLinkException ge) {
			verify(userValidator).validateUsername("TEST@TEST.COM");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testCreateUser() {
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		User user = createUser();
		User actualUser = userBAC.createUser(user);
		assertEquals(user, actualUser);
		verify(userValidator).validateUser(user);
		verify(passwordEncoder).encode("password");
		verify(userBAR).getUser("test@test.com");
		verify(userBAR).createUser(user);
		assertEquals("encodedPassword", user.getPassword());
		assertFalse(user.getActive());
		assertEquals(1, user.getRoleList().size());
		assertEquals(RoleEnum.USER, user.getRoleList().get(0));
		assertEquals("test@test.com", user.getUsername());
	}

	@Test
	public void testCreateUserWhenExist() {
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		User user = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(user);
		try {
			userBAC.createUser(user);
			fail();
		} catch (GoLinkException ge) {
			verify(userValidator).validateUser(user);
			verify(passwordEncoder).encode("password");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.already.exists", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testDeleteUser() {
		User expectedUser = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(expectedUser);
		User actualUser = userBAC.deleteUser("TEST@TEST.COM");
		assertEquals(expectedUser, actualUser);
		verify(userValidator).validateUsername("TEST@TEST.COM");
		verify(userBAR).getUser("test@test.com");
		verify(userBAR).deleteUser("TEST@test.com");
	}

	@Test
	public void testDeleteUserWhenNotExists() {
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		try {
			userBAC.deleteUser("TEST@TEST.COM");
		} catch (GoLinkException ge) {
			verify(userValidator).validateUsername("TEST@TEST.COM");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testActivateUser() {
		User expectedUser = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(expectedUser);
		User actualUser = userBAC.updateUserActivation("TEST@TEST.COM", true);
		assertEquals(expectedUser, actualUser);
		assertTrue(actualUser.getActive());
		verify(userValidator).validateUsername("TEST@TEST.COM");
		verify(userValidator).validateActivationFlag(true);
		verify(userBAR).getUser("test@test.com");
		verify(userBAR).createUser(expectedUser);
	}

	@Test
	public void testActivateUserWhenNotExists() {
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		try {
			userBAC.updateUserActivation("TEST@TEST.COM", true);
		} catch (GoLinkException ge) {
			verify(userValidator).validateUsername("TEST@TEST.COM");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testupdatePassword() {
		User expectedUser = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(expectedUser);
		when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
		User actualUser = userBAC.updateUserPassword("TEST@TEST.COM", "newPassword");
		assertEquals(expectedUser, actualUser);
		assertEquals("encodedPassword", actualUser.getPassword());
		verify(userValidator).validateUsername("TEST@TEST.COM");
		verify(userValidator).validatePassword("newPassword");
		verify(userBAR).getUser("test@test.com");
		verify(userBAR).createUser(expectedUser);
		verify(passwordEncoder).encode("newPassword");
	}

	@Test
	public void testUpdatePasswordWhenNotExists() {
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		try {
			userBAC.updateUserPassword("TEST@TEST.COM", "newPassword");
		} catch (GoLinkException ge) {
			verify(userValidator).validateUsername("TEST@TEST.COM");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	@Test
	public void testUserToRole() {
		User expectedUser = createUser();
		when(userBAR.getUser("test@test.com")).thenReturn(expectedUser);
		User actualUser = userBAC.addUserToRole("TEST@TEST.COM", RoleEnum.ADMIN);
		assertEquals(expectedUser, actualUser);
		assertEquals(RoleEnum.ADMIN, actualUser.getRoleList().get(0));
		assertEquals(1, actualUser.getRoleList().size());
		verify(userValidator).validateUsername("TEST@TEST.COM");
		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<RoleEnum>> roleCaptor = ArgumentCaptor.forClass(List.class);
		verify(userValidator).validateRoles(roleCaptor.capture());
		assertEquals(RoleEnum.ADMIN, roleCaptor.getValue().get(0));
		assertEquals(1, roleCaptor.getValue().size());
		verify(userBAR).getUser("test@test.com");
		verify(userBAR).createUser(expectedUser);
	}

	@Test
	public void testAddUserToRoleWhenNotExists() {
		when(userBAR.getUser("test@test.com")).thenReturn(null);
		try {
			userBAC.addUserToRole("TEST@TEST.COM", RoleEnum.ADMIN);
		} catch (GoLinkException ge) {
			verify(userValidator).validateUsername("TEST@TEST.COM");
			verify(userBAR).getUser("test@test.com");
			assertEquals("user.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test@test.com", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
		}
	}

	private User createUser() {
		User user = new User();
		user.setUsername("TEST@test.com");
		user.setPassword("password");
		return user;
	}

}
