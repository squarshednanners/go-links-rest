package com.go.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.go.bac.IUserBAC;
import com.go.exception.GoLinkException;
import com.go.model.User;
import com.go.security.model.GoUserDetails;

@Service
public class GoUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserBAC userBAC;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
		try {
			user = userBAC.fetchUser(username);
		} catch (GoLinkException ge) {
			throw new UsernameNotFoundException(username);
		}
		return new GoUserDetails(user);
	}

}
