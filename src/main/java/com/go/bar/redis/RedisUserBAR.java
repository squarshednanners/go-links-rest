package com.go.bar.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IUserBAR;
import com.go.model.User;

@Repository
public class RedisUserBAR implements IUserBAR {

	private static final String KEY = "user";

	@Resource(name = "userRedisTemplate")
	private HashOperations<String, String, User> hashOps;

	@Override
	public List<User> getAllUsers() {
		return new ArrayList<User>(hashOps.entries(KEY).values());
	}

	@Override
	public User getUser(String username) {
		return hashOps.get(KEY, username);
	}

	@Override
	public void createUser(User user) {
		hashOps.put(KEY, user.getUsername(), user);
	}

	@Override
	public void deleteUser(String username) {
		hashOps.delete(KEY, username);
	}

}
