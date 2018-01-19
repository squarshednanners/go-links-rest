package com.go.bar;

import java.util.List;

import com.go.model.User;

public interface IUserBAR {
	List<User> getAllUsers();

	User getUser(String username);

	void createUser(User user);

	void deleteUser(String username);
}
