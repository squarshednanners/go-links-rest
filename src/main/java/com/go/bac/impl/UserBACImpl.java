package com.go.bac.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.go.bac.IUserBAC;
import com.go.bar.IUserBAR;
import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;
import com.go.validation.UserValidator;

@Component
public class UserBACImpl implements IUserBAC {

	@Autowired
	private IUserBAR userBAR;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> fetchAllUsers() {
		return userBAR.getAllUsers();
	}

	@Override
	public User fetchUser(String username) {
		userValidator.validateUsername(username);
		username = username.toLowerCase();
		User existingUser = userBAR.getUser(username);
		if (existingUser == null) {
			throw new GoLinkException("user.does.not.exist", username);
		}
		return existingUser;
	}

	@Override
	public User createUser(User user) {
		defaultUserSettings(user);
		userValidator.validateUser(user);
		User existingUser = userBAR.getUser(user.getUsername());
		if (existingUser != null) {
			throw new GoLinkException("user.already.exists", user.getUsername());
		}
		userBAR.createUser(user);
		return user;
	}

	@Override
	public User deleteUser(String username) {
		User existingUser = fetchUser(username);
		userBAR.deleteUser(existingUser.getUsername());
		return existingUser;
	}

	@Override
	public User updateUserActivation(String username, Boolean activeFlag) {
		User existingUser = fetchUser(username);
		userValidator.validateActivationFlag(activeFlag);
		existingUser.setActive(activeFlag);
		userBAR.createUser(existingUser);
		return existingUser;
	}

	@Override
	public User updateUserPassword(String username, String password) {
		User existingUser = fetchUser(username);
		userValidator.validatePassword(password);
		existingUser.setPassword(encodePassword(password));
		userBAR.createUser(existingUser);
		return existingUser;
	}

	@Override
	public User addUserToRole(String username, RoleEnum role) {
		User existingUser = fetchUser(username);
		userValidator.validateRoles(Arrays.asList(role));
		existingUser.addRole(role);
		userBAR.createUser(existingUser);
		return existingUser;
	}

	private void defaultUserSettings(User user) {
		user.setUsername(user.getUsername().toLowerCase());
		user.setPassword(encodePassword(user.getPassword()));
		user.setActive(false);
		user.setRoleList(Arrays.asList(RoleEnum.USER));
	}

	private String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

}
