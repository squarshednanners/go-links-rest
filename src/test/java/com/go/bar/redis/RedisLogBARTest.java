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

import com.go.bar.redis.RedisLogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.UsageLog;

public class RedisLogBARTest {

	@InjectMocks
	private RedisLogBAR bar;

	@Mock
	private RedisTemplate<String, UsageLog> usageLogRedisTemplate;

	@Mock
	private ListOperations<String, UsageLog> listOps;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(usageLogRedisTemplate);
		Mockito.verifyNoMoreInteractions(listOps);
	}

	@Test
	public void testGetLogs() {
		List<UsageLog> mockUsageLogs = Arrays.asList(new UsageLog("test@test.com", "link1", ActionTypeEnum.CREATE));
		Mockito.when(listOps.range("log", 0, -1)).thenReturn(mockUsageLogs);
		List<UsageLog> actualUsageLogs = bar.getAllLogs();
		Assert.assertEquals(mockUsageLogs, actualUsageLogs);
		Assert.assertEquals(1, actualUsageLogs.size());
		Mockito.verify(listOps).range("log", 0, -1);
	}

	@Test
	public void testCreateLog() {
		UsageLog usageLog = new UsageLog("test@test.com", "link1", ActionTypeEnum.CREATE);
		bar.log(usageLog);
		Mockito.verify(listOps).rightPush("log", usageLog);
	}

}
