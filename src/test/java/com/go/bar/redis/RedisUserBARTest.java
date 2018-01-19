package com.go.bar.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.go.bar.redis.RedisUserBAR;
import com.go.model.RoleEnum;
import com.go.model.User;

public class RedisUserBARTest {

	@InjectMocks
	private RedisUserBAR bar;

	@Mock
	private RedisTemplate<String, User> userRedisTemplate;

	@Mock
	private HashOperations<String, String, User> hashOps;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(userRedisTemplate);
		Mockito.verifyNoMoreInteractions(hashOps);
	}

	@Test
	public void testGetAllUsers() {
		Map<String, User> userMap = new HashMap<>();
		User goUser = createUser();
		userMap.put("test", goUser);
		Mockito.when(hashOps.entries("user")).thenReturn(userMap);
		List<User> actualUsers = bar.getAllUsers();
		Assert.assertEquals(1, actualUsers.size());
		Assert.assertEquals(goUser, actualUsers.get(0));
		Mockito.verify(hashOps).entries("user");
	}

	@Test
	public void testGetUser() {
		User goUser = createUser();
		Mockito.when(hashOps.get("user", "test@test.com")).thenReturn(goUser);
		User actualUser = bar.getUser("test@test.com");
		Assert.assertEquals(goUser, actualUser);
		Mockito.verify(hashOps).get("user", "test@test.com");
	}

	@Test
	public void testCreateUser() {
		User user = createUser();
		bar.createUser(user);
		Mockito.verify(hashOps).put("user", "test@test.com", user);
	}

	@Test
	public void testDeleteUser() {
		bar.deleteUser("test");
		Mockito.verify(hashOps).delete("user", "test");
	}

	private User createUser() {
		User goUser = new User();
		goUser.setUsername("test@test.com");
		goUser.setPassword("password");
		goUser.setActive(true);
		goUser.addRole(RoleEnum.USER);
		return goUser;
	}

}
