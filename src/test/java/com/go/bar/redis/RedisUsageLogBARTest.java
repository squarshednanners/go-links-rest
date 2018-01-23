package com.go.bar.redis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

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

	@Test
	public void testDeleteLog() {
		Log usageLog = new Log("link1");
		bar.deleteLog(usageLog);
		Mockito.verify(listOps).remove("usageLog", 1, usageLog);
	}

	@Test
	public void testGetOldestUsageLogs() {
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 0, 1)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getOldestUsageLogs(1, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 0, 1);
	}

	@Test
	public void testGetOldestUsageLogs_SubsequentPage() {
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 2, 3)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getOldestUsageLogs(2, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 2, 3);
	}

	@Test
	public void testGetOldestUsageLogs_TenthPage() {
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 18, 19)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getOldestUsageLogs(10, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 18, 19);
	}

	@Test
	public void testGetNewestUsageLogs() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 19, 20)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getNewestUsageLogs(1, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 19, 20);
	}

	@Test
	public void testGetNewestUsageLogs_SubsequentPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 17, 18)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getNewestUsageLogs(2, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 17, 18);
	}

	@Test
	public void testGetNewestUsageLogs_TenthPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 1, 2)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getNewestUsageLogs(10, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 1, 2);
	}

	@Test
	public void testGetNewestUsageLogs_EleventhPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<Log> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", -1, 0)).thenReturn(expectedLogList);
		List<Log> actualLogList = bar.getNewestUsageLogs(11, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", -1, 0);
	}

	private List<Log> createLogList() {
		return Arrays.asList(new Log("log1"), new Log("log2"));
	}
}
