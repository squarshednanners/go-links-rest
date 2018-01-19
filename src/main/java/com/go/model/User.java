package com.go.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String password;
	private List<RoleEnum> roleList;
	private Boolean active;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleEnum> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleEnum> roleList) {
		this.roleList = roleList;
	}

	public void addRole(RoleEnum role) {
		if (roleList == null) {
			roleList = new ArrayList<RoleEnum>();
		}
		roleList.add(role);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
