package com.go.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.go.bac.IUserBAC;
import com.go.model.Message;
import com.go.model.Response;
import com.go.model.RoleEnum;
import com.go.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

	@Autowired
	private IUserBAC userBAC;

	@Value("${user.activate.upon.creation}")
	private boolean activateUserOnCreation;

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<List<User>> fetchAllUsers() {
		Response<List<User>> response = new Response<List<User>>();
		try {
			response.setResults(userBAC.fetchAllUsers());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.fetch.error"));
		}
		return response;
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<User> deleteUser(@RequestParam("username") String username) {
		Response<User> response = new Response<User>();
		try {
			response.setResults(userBAC.deleteUser(username));
			formatSuccessResponse(response, new Message("user.deleted"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.delete.error"));
		}
		return response;
	}

	@PostMapping("/create")
	@PreAuthorize("permitAll")
	public Response<User> createUser(@RequestBody User user) {
		Response<User> response = new Response<User>();
		try {
			User createdUser = userBAC.createUser(user);
			response.setResults(createdUser);
			formatSuccessResponse(response, new Message("user.created"));
			if (activateUserOnCreation) {
				activateUser(response, createdUser.getUsername(), true);
			}
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.create.error"));
		}
		return response;
	}

	@GetMapping("/activate")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<User> activateUser(@RequestParam("username") String username,
			@RequestParam("active") boolean active) {
		Response<User> response = new Response<User>();
		activateUser(response, username, active);
		return response;
	}

	private void activateUser(Response<User> response, String username, boolean active) {
		try {
			response.setResults(userBAC.updateUserActivation(username, active));
			formatSuccessResponse(response, new Message("user.activated"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.activation.error"));
		}
	}

	@GetMapping("/addRole")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<User> addUserRole(@RequestParam("username") String username, @RequestParam("role") RoleEnum role) {
		Response<User> response = new Response<User>();
		try {
			response.setResults(userBAC.addUserToRole(username, role));
			formatSuccessResponse(response, new Message("user.role.added"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.role.error"));
		}
		return response;
	}

	@GetMapping("/changePassword")
	@PreAuthorize("hasRole('USER')")
	public Response<User> changePassword(@RequestParam("password") String password, Principal principal) {
		Response<User> response = new Response<User>();
		try {
			response.setResults(userBAC.updateUserPassword(principal.getName(), password));
			formatSuccessResponse(response, new Message("user.password.changed"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.password.error"));
		}
		return response;
	}

	@GetMapping("/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<User> fetchUser(@PathVariable("username") String username) {
		Response<User> response = new Response<User>();
		try {
			response.setResults(userBAC.fetchUser(username));
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("user.fetch.error"));
		}
		return response;
	}
}
