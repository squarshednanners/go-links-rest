package com.go.validation;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;

@Component
public class UserValidator {
	public void validateUser(User user) {
		validateUsername(user.getUsername());
		validatePassword(user.getPassword());
		validateActivationFlag(user.getActive());
		validateRoles(user.getRoleList());
	}

	public void validateUsername(String name) {
		if (name == null || StringUtils.isEmpty(name)) {
			throw new GoLinkException("user.name.required");
		}
		if (!EmailValidator.getInstance().isValid(name)) {
			throw new GoLinkException("user.name.invalid", name);
		}
	}

	public void validatePassword(String password) {
		if (password == null || StringUtils.isEmpty(password)) {
			throw new GoLinkException("user.password.required");
		}
	}

	public void validateActivationFlag(Boolean activeFlag) {
		if (activeFlag == null) {
			throw new GoLinkException("user.activation.required");
		}
	}

	public void validateRoles(List<RoleEnum> roles) {
		if (roles == null || roles.isEmpty()) {
			throw new GoLinkException("user.role.required");
		}
	}
}
