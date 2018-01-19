package com.go.bac;

import java.util.List;

import com.go.model.RoleEnum;
import com.go.model.User;

public interface IUserBAC {

	List<User> fetchAllUsers();

	User fetchUser(String username);

	User createUser(User user);

	User updateUserActivation(String username, Boolean active);

	User updateUserPassword(String username, String password);

	User addUserToRole(String username, RoleEnum role);

	User deleteUser(String username);
}
