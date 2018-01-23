package com.go.bar.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.go.model.Log;

public class RedisUsageLogBARTest {

	@InjectMocks
	private RedisUsageLogBAR bar;

	@Mock
	private RedisTemplate<String, Log> usageLogRedisTemplate;

	@Mock
	private ListOperations<String, Log> listOps;

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
	public void testCreateLog() {
		Log usageLog = new Log("link1");
		bar.log(usageLog);
		Mockito.verify(listOps).rightPush("usageLog", usageLog);
	}
}
