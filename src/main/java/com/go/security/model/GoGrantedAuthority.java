package com.go.security.model;

import org.springframework.security.core.GrantedAuthority;

import com.go.model.RoleEnum;

@SuppressWarnings("serial")
public class GoGrantedAuthority implements GrantedAuthority {
	private final RoleEnum role;

	public GoGrantedAuthority(RoleEnum role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + role.toString();
	}
}
