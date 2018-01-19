package com.go.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.go.bac.IUserBAC;
import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;
import com.go.security.service.GoUserDetailsService;

public class GoUserDetailsServiceTest {

	@InjectMocks
	private GoUserDetailsService userService;

	@Mock
	private IUserBAC userBAC;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(userBAC);
	}

	@Test
	public void testLoadUser() {
		User user = createUser();
		Mockito.when(userBAC.fetchUser("test@test.com")).thenReturn(user);
		UserDetails userDetails = userService.loadUserByUsername("test@test.com");
		assertTrue(userDetails.isEnabled());
		assertTrue(userDetails.isAccountNonExpired());
		assertTrue(userDetails.isAccountNonLocked());
		assertTrue(userDetails.isCredentialsNonExpired());
		assertEquals("pass", userDetails.getPassword());
		assertEquals("test1@test.com", userDetails.getUsername());
		assertEquals(1, userDetails.getAuthorities().size());
		assertEquals("ROLE_USER", ((GrantedAuthority) userDetails.getAuthorities().toArray()[0]).getAuthority());
		Mockito.verify(userBAC).fetchUser("test@test.com");
	}

	@Test
	public void testLoadUserWhenNotExists() {
		Mockito.when(userBAC.fetchUser("test@test.com")).thenThrow(new GoLinkException("ABC"));
		try {
			userService.loadUserByUsername("test@test.com");
			fail();
		} catch (UsernameNotFoundException unfe) {
			Mockito.verify(userBAC).fetchUser("test@test.com");
		}
	}

	private User createUser() {
		User user = new User();
		user.setActive(true);
		user.setUsername("test1@test.com");
		user.setPassword("pass");
		user.addRole(RoleEnum.USER);
		return user;
	}

}
