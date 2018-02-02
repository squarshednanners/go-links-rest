package com.go.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.util.ReflectionTestUtils;

import com.go.bac.IUserBAC;
import com.go.exception.GoLinkException;
import com.go.model.Response;
import com.go.model.RoleEnum;
import com.go.model.User;

public class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private IUserBAC userBAC;

	@Mock
	private MessageSourceAccessor messageSourceAccessor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(userBAC);
		verifyNoMoreInteractions(messageSourceAccessor);
	}

	@Test
	public void testGetAllUsers() {
		List<User> userList = Arrays.asList(createUser());
		when(userBAC.fetchAllUsers()).thenReturn(userList);
		Response<List<User>> response = userController.fetchAllUsers();
		assertEquals(userList, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(userBAC).fetchAllUsers();
	}

	@Test
	public void testGetAllUsersOnException() {
		when(userBAC.fetchAllUsers()).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<List<User>> response = userController.fetchAllUsers();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).fetchAllUsers();
		verify(messageSourceAccessor).getMessage(eq("user.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetAllUsersOnGoLinkException() {
		when(userBAC.fetchAllUsers()).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<User>> response = userController.fetchAllUsers();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).fetchAllUsers();
		verify(messageSourceAccessor).getMessage(eq("user.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testGetUser() {
		User user = createUser();
		when(userBAC.fetchUser("test@test.com")).thenReturn(user);
		Response<User> response = userController.fetchUser("test@test.com");
		assertEquals(user, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(userBAC).fetchUser("test@test.com");
	}

	@Test
	public void testGetUserOnException() {
		when(userBAC.fetchUser("test@test.com")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.fetchUser("test@test.com");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).fetchUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("user.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetUserOnGoLinkException() {
		when(userBAC.fetchUser("test@test.com")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.fetchUser("test@test.com");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).fetchUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("user.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testCreateUser() {
		User createUser = createUser();
		when(userBAC.createUser(createUser)).thenReturn(createUser);
		when(messageSourceAccessor.getMessage(eq("user.created"), any(Object[].class))).thenReturn("parsed message");
		Response<User> response = userController.createUser(createUser);
		assertEquals(createUser, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(userBAC).createUser(createUser);
		verify(messageSourceAccessor).getMessage(eq("user.created"), any(Object[].class));
	}

	@Test
	public void testCreateUserOnException() {
		User createUser = createUser();
		when(userBAC.createUser(createUser)).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.create.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.createUser(createUser);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).createUser(createUser);
		verify(messageSourceAccessor).getMessage(eq("user.create.error"), any(Object[].class));
	}

	@Test
	public void testCreateUserOnGoLinkException() {
		User createUser = createUser();
		when(userBAC.createUser(createUser)).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.create.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.createUser(createUser);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).createUser(createUser);
		verify(messageSourceAccessor).getMessage(eq("user.create.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testCreateAndActivateUser() {
		ReflectionTestUtils.setField(userController, "activateUserOnCreation", true);
		User createUser = createUser();
		User activatedUser = createUser();
		when(userBAC.createUser(createUser)).thenReturn(createUser);
		when(userBAC.updateUserActivation("test@test.com", true)).thenReturn(activatedUser);
		when(messageSourceAccessor.getMessage(eq("user.created"), any(Object[].class)))
				.thenReturn("parsed created message");
		when(messageSourceAccessor.getMessage(eq("user.activated"), any(Object[].class)))
				.thenReturn("parsed activated message");
		Response<User> response = userController.createUser(createUser);
		assertEquals(activatedUser, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed created message", response.getMessageList().get(0));
		assertEquals("parsed activated message", response.getMessageList().get(1));
		verify(userBAC).createUser(createUser);
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.created"), any(Object[].class));
		verify(messageSourceAccessor).getMessage(eq("user.activated"), any(Object[].class));
	}

	@Test
	public void testCreateAndActivateUserOnActivationException() {
		ReflectionTestUtils.setField(userController, "activateUserOnCreation", true);
		User createUser = createUser();
		when(userBAC.createUser(createUser)).thenReturn(createUser);
		when(userBAC.updateUserActivation("test@test.com", true))
				.thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.created"), any(Object[].class)))
				.thenReturn("parsed created message");
		when(messageSourceAccessor.getMessage(eq("user.activation.error"), any(Object[].class)))
				.thenReturn("parsed activation error message");
		Response<User> response = userController.createUser(createUser);
		assertEquals(createUser, response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(3, response.getMessageList().size());
		assertEquals("parsed created message", response.getMessageList().get(0));
		assertEquals("parsed activation error message", response.getMessageList().get(1));
		assertEquals("Bad things happened", response.getMessageList().get(2));
		verify(userBAC).createUser(createUser);
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.created"), any(Object[].class));
		verify(messageSourceAccessor).getMessage(eq("user.activation.error"), any(Object[].class));
	}
	
	@Test
	public void testCreateAndActivateUserOnActivationGoLinkException() {
		ReflectionTestUtils.setField(userController, "activateUserOnCreation", true);
		User createUser = createUser();
		when(userBAC.createUser(createUser)).thenReturn(createUser);
		when(userBAC.updateUserActivation("test@test.com", true)).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.created"), any(Object[].class)))
				.thenReturn("parsed created message");
		when(messageSourceAccessor.getMessage(eq("user.activation.error"), any(Object[].class)))
				.thenReturn("parsed activation error message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.createUser(createUser);
		assertEquals(createUser, response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(3, response.getMessageList().size());
		assertEquals("parsed created message", response.getMessageList().get(0));
		assertEquals("parsed activation error message", response.getMessageList().get(1));
		assertEquals("parsed message 2", response.getMessageList().get(2));
		verify(userBAC).createUser(createUser);
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.created"), any(Object[].class));
		verify(messageSourceAccessor).getMessage(eq("user.activation.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testDeleteUser() {
		User deleteUser = createUser();
		when(userBAC.deleteUser("test@test.com")).thenReturn(deleteUser);
		when(messageSourceAccessor.getMessage(eq("user.deleted"), any(Object[].class))).thenReturn("parsed message");
		Response<User> response = userController.deleteUser("test@test.com");
		assertEquals(deleteUser, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(userBAC).deleteUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("user.deleted"), any(Object[].class));
	}

	@Test
	public void testDeleteUserOnException() {
		when(userBAC.deleteUser("test@test.com")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.delete.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.deleteUser("test@test.com");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).deleteUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("user.delete.error"), any(Object[].class));
	}

	@Test
	public void testDeleteUserOnGoLinkException() {
		when(userBAC.deleteUser("test@test.com")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.delete.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.deleteUser("test@test.com");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).deleteUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("user.delete.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testActivateUser() {
		User activatedUser = createUser();
		when(userBAC.updateUserActivation("test@test.com", true)).thenReturn(activatedUser);
		when(messageSourceAccessor.getMessage(eq("user.activated"), any(Object[].class))).thenReturn("parsed message");
		Response<User> response = userController.activateUser("test@test.com", true);
		assertEquals(activatedUser, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.activated"), any(Object[].class));
	}

	@Test
	public void testActivateUserOnException() {
		when(userBAC.updateUserActivation("test@test.com", true))
				.thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.activation.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.activateUser("test@test.com", true);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.activation.error"), any(Object[].class));
	}

	@Test
	public void testActivateUserOnGoLinkException() {
		when(userBAC.updateUserActivation("test@test.com", true)).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.activation.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.activateUser("test@test.com", true);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).updateUserActivation("test@test.com", true);
		verify(messageSourceAccessor).getMessage(eq("user.activation.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testAddUserRole() {
		User user = createUser();
		when(userBAC.addUserToRole("test@test.com", RoleEnum.ADMIN)).thenReturn(user);
		when(messageSourceAccessor.getMessage(eq("user.role.added"), any(Object[].class))).thenReturn("parsed message");
		Response<User> response = userController.addUserRole("test@test.com", RoleEnum.ADMIN);
		assertEquals(user, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(userBAC).addUserToRole("test@test.com", RoleEnum.ADMIN);
		verify(messageSourceAccessor).getMessage(eq("user.role.added"), any(Object[].class));
	}

	@Test
	public void testAddUserRoleOnException() {
		when(userBAC.addUserToRole("test@test.com", RoleEnum.ADMIN))
				.thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.role.error"), any(Object[].class))).thenReturn("parsed message");
		Response<User> response = userController.addUserRole("test@test.com", RoleEnum.ADMIN);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).addUserToRole("test@test.com", RoleEnum.ADMIN);
		verify(messageSourceAccessor).getMessage(eq("user.role.error"), any(Object[].class));
	}

	@Test
	public void testAddUserRoleOnGoLinkException() {
		when(userBAC.addUserToRole("test@test.com", RoleEnum.ADMIN))
				.thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.role.error"), any(Object[].class))).thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.addUserRole("test@test.com", RoleEnum.ADMIN);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).addUserToRole("test@test.com", RoleEnum.ADMIN);
		verify(messageSourceAccessor).getMessage(eq("user.role.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testUpdatePassword() {
		User user = createUser();
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("test@test.com");
		when(userBAC.updateUserPassword("test@test.com", "password")).thenReturn(user);
		when(messageSourceAccessor.getMessage(eq("user.password.changed"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.changePassword("password", principal);
		assertEquals(user, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(userBAC).updateUserPassword("test@test.com", "password");
		verify(messageSourceAccessor).getMessage(eq("user.password.changed"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testUpdatePasswordOnException() {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("test@test.com");
		when(userBAC.updateUserPassword("test@test.com", "password"))
				.thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("user.password.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<User> response = userController.changePassword("password", principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(userBAC).updateUserPassword("test@test.com", "password");
		verify(messageSourceAccessor).getMessage(eq("user.password.error"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testUpdatePasswordOnGoLinkException() {
		Principal principal = Mockito.mock(Principal.class);
		when(principal.getName()).thenReturn("test@test.com");
		when(userBAC.updateUserPassword("test@test.com", "password"))
				.thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("user.password.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<User> response = userController.changePassword("password", principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(userBAC).updateUserPassword("test@test.com", "password");
		verify(messageSourceAccessor).getMessage(eq("user.password.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
		verify(principal).getName();
	}

	private User createUser() {
		User user = new User();
		user.setUsername("test@test.com");
		return user;
	}

}
