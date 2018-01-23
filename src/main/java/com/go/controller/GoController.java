package com.go.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.go.bac.IGoBAC;
import com.go.bac.IUsageLogBAC;
import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.Message;
import com.go.model.Response;
import com.go.url.UrlBuilder;

@RestController
@RequestMapping("/api/go")
public class GoController extends BaseController {

	@Autowired
	private IGoBAC goBAC;

	@Autowired
	private IUsageLogBAC usageLogBAC;

	@Value("${link.does.not.exist.url}")
	private String linkNotExistsUrl;

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER')")
	public Response<List<GoLink>> fetchAllLinks() {
		Response<List<GoLink>> response = new Response<List<GoLink>>();
		try {
			response.setResults(goBAC.fetchAllLinks());
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("go.link.fetch.error"));
		}
		return response;
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public Response<GoLink> deleteLink(@RequestParam("name") String linkName, Principal principal) {
		Response<GoLink> response = new Response<GoLink>();
		try {
			response.setResults(goBAC.deleteLink(linkName, principal.getName()));
			formatSuccessResponse(response, new Message("go.link.deleted"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("go.link.delete.error"));
		}
		return response;
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public Response<GoLink> createLink(@RequestBody GoLink link, Principal principal) {
		Response<GoLink> response = new Response<GoLink>();
		try {
			response.setResults(goBAC.createLink(link, principal.getName()));
			formatSuccessResponse(response, new Message("go.link.created"));
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("go.link.create.error"));
		}
		return response;
	}

	@PostMapping("/search")
	@PreAuthorize("hasRole('USER')")
	public Response<List<GoLink>> searchLinks(@RequestBody String searchString) {
		Response<List<GoLink>> response = new Response<List<GoLink>>();
		try {
			response.setResults(goBAC.searchLinks(searchString));
			formatSuccessResponse(response);
		} catch (Exception e) {
			formatErrorResponse(response, e, new Message("go.link.fetch.error"));
		}
		return response;
	}

	@GetMapping("/route/{name}/{param}")
	@PreAuthorize("permitAll")
	public Response<GoLink> go(@PathVariable("name") String name, @PathVariable("param") String param,
			HttpServletResponse httpServletResponse) {
		try {
			GoLink link = goBAC.fetchLink(name);
			redirect(link, param, httpServletResponse);
			logUsage(link.getName());
			return null;
		} catch (Exception e) {
			Response<GoLink> response = new Response<GoLink>();
			formatErrorResponse(response, e, new Message("go.link.fetch.error"));
			return response;
		}
	}

	@GetMapping("/route/{name}")
	@PreAuthorize("permitAll")
	public Response<GoLink> go(@PathVariable("name") String name, HttpServletResponse httpServletResponse) {
		try {
			GoLink link = goBAC.fetchLink(name);
			redirect(link, null, httpServletResponse);
			logUsage(link.getName());
			return null;
		} catch (Exception e) {
			if (e instanceof GoLinkException
					&& "go.link.does.not.exist".equals(((GoLinkException) e).getErrorMessage().getCode())) {
				redirect(linkNotExistsUrl.replace("{linkName}", name), httpServletResponse);
				return null;
			} else {
				Response<GoLink> response = new Response<GoLink>();
				formatErrorResponse(response, e, new Message("go.link.fetch.error"));
				return response;
			}
		}
	}

	private void redirect(GoLink link, String param, HttpServletResponse httpServletResponse) {
		redirect(UrlBuilder.buildUrl(link, param), httpServletResponse);
	}

	private void redirect(String url, HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(302);
		httpServletResponse.setHeader("Location", url);
	}

	private void logUsage(String linkName) {
		usageLogBAC.logUsage(linkName);
	}

}
