package com.go.bar.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.go.bar.IGoLinkBAR;
import com.go.model.GoLink;

@Repository
public class RedisGoLinkBAR implements IGoLinkBAR {

	private static final String KEY = "link";

	@Resource(name = "goLinkRedisTemplate")
	private HashOperations<String, String, GoLink> hashOps;

	@Override
	public List<GoLink> getAllLinks() {
		return new ArrayList<GoLink>(hashOps.entries(KEY).values());
	}

	@Override
	public GoLink getLink(String linkName) {
		return hashOps.get(KEY, linkName);
	}

	@Override
	public void createLink(GoLink link) {
		hashOps.put(KEY, link.getName(), link);
	}

	@Override
	public void deleteLink(String linkName) {
		hashOps.delete(KEY, linkName);
	}

}
