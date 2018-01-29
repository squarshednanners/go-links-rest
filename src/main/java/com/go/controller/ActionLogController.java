package com.go.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.go.bac.IActionLogBAC;
import com.go.model.Message;
import com.go.model.Response;
import com.go.model.SortDirection;
import com.go.model.ActionLog;

@RestController
@RequestMapping("/api/log/action")
public class ActionLogController extends BaseController {

	@Autowired
	private IActionLogBAC actionLogBAC;

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<List<ActionLog>> fetchAllLogs(
			@RequestParam(value = "sortDirection", required = false) SortDirection sortDirection) {
		Response<List<ActionLog>> response = new Response<List<ActionLog>>();
		try {
			response.setResults(actionLogBAC.fetchAllLogs(sortDirection));
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("log.fetch.error"));
		}
		return response;
	}

	@GetMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<List<ActionLog>> fetchUserLogs(@PathVariable("username") String username) {
		Response<List<ActionLog>> response = new Response<List<ActionLog>>();
		try {
			response.setResults(actionLogBAC.fetchLogsForUser(username));
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("log.fetch.error"));
		}
		return response;
	}

}
