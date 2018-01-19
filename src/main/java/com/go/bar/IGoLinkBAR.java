package com.go.bar;

import java.util.List;

import com.go.model.GoLink;

public interface IGoLinkBAR {

	List<GoLink> getAllLinks();

	GoLink getLink(String linkName);

	void createLink(GoLink link);

	void deleteLink(String linkName);
}
