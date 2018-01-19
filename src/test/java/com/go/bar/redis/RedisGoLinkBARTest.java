package com.go.bar.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.go.bar.redis.RedisGoLinkBAR;
import com.go.model.GoLink;

public class RedisGoLinkBARTest {

	@InjectMocks
	private RedisGoLinkBAR bar;

	@Mock
	private RedisTemplate<String, GoLink> goLinkRedisTemplate;

	@Mock
	private HashOperations<String, String, GoLink> hashOps;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(goLinkRedisTemplate);
		Mockito.verifyNoMoreInteractions(hashOps);
	}

	@Test
	public void testGetAllLinks() {
		Map<String, GoLink> linkMap = new HashMap<>();
		GoLink goLink = createGoLink("test", "http://test.com");
		linkMap.put("test", goLink);
		Mockito.when(hashOps.entries("link")).thenReturn(linkMap);
		List<GoLink> actualLinks = bar.getAllLinks();
		Assert.assertEquals(1, actualLinks.size());
		Assert.assertEquals(goLink, actualLinks.get(0));
		Mockito.verify(hashOps).entries("link");
	}

	@Test
	public void testGetLink() {
		GoLink goLink = createGoLink("test", "http://test.com");
		Mockito.when(hashOps.get("link", "test")).thenReturn(goLink);
		GoLink actualLink = bar.getLink("test");
		Assert.assertEquals(goLink, actualLink);
		Mockito.verify(hashOps).get("link", "test");
	}

	@Test
	public void testCreateLink() {
		GoLink link = createGoLink("test", "http://test.com");
		bar.createLink(link);
		Mockito.verify(hashOps).put("link", "test", link);
	}

	@Test
	public void testDeleteLink() {
		bar.deleteLink("test");
		Mockito.verify(hashOps).delete("link", "test");
	}

	private GoLink createGoLink(String name, String url) {
		GoLink goLink = new GoLink();
		goLink.setName(name);
		goLink.setUrl(url);
		return goLink;
	}
}
