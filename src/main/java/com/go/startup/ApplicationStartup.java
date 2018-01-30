package com.go.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.go.bac.IUserBAC;
import com.go.exception.GoLinkException;
import com.go.model.RoleEnum;
import com.go.model.User;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	final static Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	private IUserBAC userBAC;

	@Value("${default.admin.username}")
	private String defaultAdminUsername;

	@Value("${default.admin.password}")
	private String defaultAdminPassword;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		try {
			userBAC.fetchUser(defaultAdminUsername);
			LOGGER.debug("Default Admin User: " + defaultAdminUsername + " already exists");
		} catch (GoLinkException ge) {
			if ("user.does.not.exist".equals(ge.getErrorMessage().getCode())) {
				LOGGER.info("Creating Default Admin User: " + defaultAdminUsername);
				userBAC.createUser(createUser(defaultAdminUsername, defaultAdminPassword));
				userBAC.addUserToRole(defaultAdminUsername, RoleEnum.ADMIN);
				userBAC.updateUserActivation(defaultAdminUsername, true);
			} else {
				throw ge;
			}
		}
	}

	private User createUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}

}