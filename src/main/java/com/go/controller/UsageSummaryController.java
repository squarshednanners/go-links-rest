package com.go.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.go.bac.IUsageSummaryBAC;
import com.go.model.Message;
import com.go.model.Response;
import com.go.model.UsageSummary;

@RestController
@RequestMapping("/api/summary/usage")
public class UsageSummaryController extends BaseController {

	@Autowired
	private IUsageSummaryBAC usageSummaryLogBAC;

	@GetMapping("/total")
	@PreAuthorize("hasRole('USER')")
	public Response<UsageSummary> fetchTotalSummary() {
		Response<UsageSummary> response = new Response<UsageSummary>();
		try {
			response.setResults(usageSummaryLogBAC.fetchTotalSummary());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("summary.fetch.error"));
		}
		return response;
	}

	@GetMapping("/hour")
	@PreAuthorize("hasRole('USER')")
	public Response<List<UsageSummary>> fetchHourlySummaries() {
		Response<List<UsageSummary>> response = new Response<List<UsageSummary>>();
		try {
			response.setResults(usageSummaryLogBAC.fetchAllHourlySummaries());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("summary.fetch.error"));
		}
		return response;
	}

	@GetMapping("/day")
	@PreAuthorize("hasRole('USER')")
	public Response<List<UsageSummary>> fetchDailySummaries() {
		Response<List<UsageSummary>> response = new Response<List<UsageSummary>>();
		try {
			response.setResults(usageSummaryLogBAC.fetchAllDailySummaries());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("summary.fetch.error"));
		}
		return response;
	}
}
