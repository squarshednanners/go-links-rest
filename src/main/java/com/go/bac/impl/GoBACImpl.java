package com.go.bac.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go.bac.IGoBAC;
import com.go.bac.ILogBAC;
import com.go.bar.IGoLinkBAR;
import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.validation.GoLinkValidator;

@Component
public class GoBACImpl implements IGoBAC {

	@Autowired
	private IGoLinkBAR goBAR;

	@Autowired
	private ILogBAC usageBAC;

	@Autowired
	private GoLinkValidator goLinkValidator;

	@Override
	public List<GoLink> fetchAllLinks() {
		return goBAR.getAllLinks();
	}

	@Override
	public GoLink fetchLink(String linkName) {
		goLinkValidator.validateName(linkName);
		linkName = linkName.toLowerCase();
		GoLink existingLink = goBAR.getLink(linkName);
		if (existingLink == null) {
			throw new GoLinkException("go.link.does.not.exist", linkName);
		}
		return existingLink;
	}

	@Override
	public GoLink createLink(GoLink link, String requestingUser) {
		defaultGoLinkData(link);
		goLinkValidator.validateLink(link);
		GoLink existingLink = goBAR.getLink(link.getName());
		if (existingLink != null) {
			throw new GoLinkException("go.link.already.exists", existingLink.getName());
		}
		goBAR.createLink(link);
		usageBAC.logLinkCreation(requestingUser, link.getName());
		return link;
	}

	@Override
	public GoLink deleteLink(String linkName, String requestingUser) {
		GoLink existingLink = fetchLink(linkName);
		goBAR.deleteLink(existingLink.getName());
		usageBAC.logLinkDeletion(requestingUser, existingLink.getName());
		return existingLink;
	}

	private void defaultGoLinkData(GoLink goLink) {
		if (goLink.getName() != null) {
			goLink.setName(goLink.getName().toLowerCase().trim());
		}
		if (goLink.getParameter() != null && goLink.getParameter().getType() == null) {
			goLink.setParameter(null);
		}
	}

	@Override
	public List<GoLink> searchLinks(String searchString) {
		// TODO decide if we should store hashmap with URL as the key for fast searches?
		// tradeoff is duplicating GoLink data across two hashmaps
		// We can already search on link name using hscan just not url values
		List<GoLink> linkList = new ArrayList<GoLink>();
		for (GoLink link : goBAR.getAllLinks()) {
			if (link.getName().contains(searchString.toLowerCase()) || link.getUrl().contains(searchString)) {
				linkList.add(link);
			}
		}
		return linkList;
	}

}
