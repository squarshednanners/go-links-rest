package com.go.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.go.bac.ILogBAC;
import com.go.model.Message;
import com.go.model.Response;
import com.go.model.UsageLog;

@RestController
@RequestMapping("/api/log")
public class LogController extends BaseController {

	@Autowired
	private ILogBAC usageLogBAC;

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<List<UsageLog>> fetchAllLogs() {
		Response<List<UsageLog>> response = new Response<List<UsageLog>>();
		try {
			response.setResults(usageLogBAC.fetchAllLogs());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("log.fetch.error"));
		}
		return response;
	}

	@GetMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public Response<List<UsageLog>> fetchUserLogs(@PathVariable("username") String username) {
		Response<List<UsageLog>> response = new Response<List<UsageLog>>();
		try {
			response.setResults(usageLogBAC.fetchLogsForUser(username));
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("log.fetch.error"));
		}
		return response;
	}

}
