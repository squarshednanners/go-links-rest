package com.go.bar.redis;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.go.bar.redis.RedisActionLogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.SortDirection;
import com.go.model.ActionLog;

public class RedisActionLogBARTest {

	@InjectMocks
	private RedisActionLogBAR bar;

	@Mock
	private RedisTemplate<String, ActionLog> actionLogRedisTemplate;

	@Mock
	private ListOperations<String, ActionLog> listOps;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(actionLogRedisTemplate);
		Mockito.verifyNoMoreInteractions(listOps);
	}

	@Test
	public void testGetLogs_WithNoSort() {
		List<ActionLog> mockActionLogs = Arrays.asList(new ActionLog("test@test.com", "link1", ActionTypeEnum.CREATE),
				new ActionLog("test@test.com", "link2", ActionTypeEnum.DELETE));
		Mockito.when(listOps.range("actionLog", 0, -1)).thenReturn(mockActionLogs);
		List<ActionLog> actualActionLogs = bar.getAllLogs(null);
		Assert.assertEquals(mockActionLogs, actualActionLogs);
		Assert.assertEquals(2, actualActionLogs.size());
		Assert.assertEquals("link1", actualActionLogs.get(0).getLinkName());
		Mockito.verify(listOps).range("actionLog", 0, -1);
	}

	@Test
	public void testGetLogs_WithAscSort() {
		List<ActionLog> mockActionLogs = Arrays.asList(new ActionLog("test@test.com", "link1", ActionTypeEnum.CREATE),
				new ActionLog("test@test.com", "link2", ActionTypeEnum.DELETE));
		Mockito.when(listOps.range("actionLog", 0, -1)).thenReturn(mockActionLogs);
		List<ActionLog> actualActionLogs = bar.getAllLogs(SortDirection.ASC);
		Assert.assertEquals(mockActionLogs, actualActionLogs);
		Assert.assertEquals(2, actualActionLogs.size());
		Assert.assertEquals("link1", actualActionLogs.get(0).getLinkName());
		Mockito.verify(listOps).range("actionLog", 0, -1);
	}

	@Test
	public void testGetLogs_WithDescSort() {
		List<ActionLog> mockActionLogs = Arrays.asList(new ActionLog("test@test.com", "link1", ActionTypeEnum.CREATE),
				new ActionLog("test@test.com", "link2", ActionTypeEnum.DELETE));
		Mockito.when(listOps.range("actionLog", 0, -1)).thenReturn(mockActionLogs);
		List<ActionLog> actualActionLogs = bar.getAllLogs(SortDirection.DESC);
		Assert.assertEquals(mockActionLogs, actualActionLogs);
		Assert.assertEquals(2, actualActionLogs.size());
		Assert.assertEquals("link2", actualActionLogs.get(0).getLinkName());
		Mockito.verify(listOps).range("actionLog", 0, -1);
	}

	@Test
	public void testCreateLog() {
		ActionLog actionLog = new ActionLog("test@test.com", "link1", ActionTypeEnum.CREATE);
		bar.log(actionLog);
		Mockito.verify(listOps).rightPush("actionLog", actionLog);
	}

}
