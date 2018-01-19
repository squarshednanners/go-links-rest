package com.go.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.go.model.RoleEnum;
import com.go.model.User;

@SuppressWarnings("serial")
public class GoUserDetails implements UserDetails {
	private final User user;

	public GoUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return convertRolesToAuthorities(user.getRoleList());
	}

	private Collection<? extends GrantedAuthority> convertRolesToAuthorities(List<RoleEnum> roleList) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (RoleEnum role : roleList) {
			grantedAuthorities.add(new GoGrantedAuthority(role));
		}
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getActive();
	}

}
