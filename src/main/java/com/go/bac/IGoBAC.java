package com.go.bac;

import java.util.List;

import com.go.model.GoLink;

public interface IGoBAC {

	List<GoLink> fetchAllLinks();

	GoLink fetchLink(String linkName);

	GoLink createLink(GoLink link, String requestingUser);

	GoLink deleteLink(String linkName, String requestingUser);

	List<GoLink> searchLinks(String searchString);

}
