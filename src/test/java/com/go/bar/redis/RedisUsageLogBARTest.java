package com.go.bar.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import com.go.model.SortDirection;
import com.go.model.UsageLog;

public class RedisUsageLogBARTest {

	@InjectMocks
	private RedisUsageLogBAR bar;

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
	public void testCreateLog() {
		UsageLog usageLog = new UsageLog("link1");
		bar.log(usageLog);
		Mockito.verify(listOps).rightPush("usageLog", usageLog);
	}

	@Test
	public void testDeleteLog() {
		UsageLog usageLog = new UsageLog("link1");
		bar.deleteLog(usageLog);
		Mockito.verify(listOps).remove("usageLog", 1, usageLog);
	}

	@Test
	public void testGetOldestUsageLogs() {
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 0, 1)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedAscending(1, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 0, 1);
	}

	@Test
	public void testGetOldestUsageLogs_SubsequentPage() {
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 2, 3)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedAscending(2, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 2, 3);
	}

	@Test
	public void testGetOldestUsageLogs_TenthPage() {
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 18, 19)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedAscending(10, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).range("usageLog", 18, 19);
	}

	@Test
	public void testGetNewestUsageLogs() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 19, 20)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedDescending(1, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 19, 20);
	}

	@Test
	public void testGetNewestUsageLogs_SubsequentPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 17, 18)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedDescending(2, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 17, 18);
	}

	@Test
	public void testGetNewestUsageLogs_TenthPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", 1, 2)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedDescending(10, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 1, 2);
	}

	@Test
	public void testGetNewestUsageLogs_EleventhPage() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		List<UsageLog> expectedLogList = createLogList();
		Mockito.when(listOps.range("usageLog", -1, 0)).thenReturn(expectedLogList);
		List<UsageLog> actualLogList = bar.fetchPagedLogsSortedDescending(11, 2);
		assertEquals(expectedLogList, actualLogList);
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", -1, 0);
	}

	@Test
	public void testFetchLogsByInterval_SortAsc() {
		Mockito.when(listOps.range("usageLog", 0, 49))
				.thenReturn(Arrays.asList(createLog(0), createLog(1), createLog(2)));
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.ASC);
		assertEquals(2, actualLogs.size());
		assertEquals(0l, actualLogs.get(0).getTime().longValue());
		assertEquals(1l, actualLogs.get(1).getTime().longValue());
		Mockito.verify(listOps).range("usageLog", 0, 49);
	}

	@Test
	public void testFetchLogsByIntervalWithSubsequentFetchDespiteOutOfBoundsTime_SortAsc() {
		Mockito.when(listOps.range("usageLog", 0, 49))
				.thenReturn(Arrays.asList(createLog(0), createLog(1), createLog(-1)));
		Mockito.when(listOps.range("usageLog", 50, 99)).thenReturn(Arrays.asList());
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.ASC);
		assertEquals(2, actualLogs.size());
		assertEquals(0l, actualLogs.get(0).getTime().longValue());
		assertEquals(1l, actualLogs.get(1).getTime().longValue());
		Mockito.verify(listOps).range("usageLog", 0, 49);
		Mockito.verify(listOps).range("usageLog", 50, 99);
	}

	@Test
	public void testFetchLogsByIntervalWithEmptyList_SortAsc() {
		Mockito.when(listOps.range("usageLog", 0, 49)).thenReturn(Arrays.asList());
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.ASC);
		assertTrue(actualLogs.isEmpty());
		Mockito.verify(listOps).range("usageLog", 0, 49);
	}

	@Test
	public void testFetchLogsByInterval_SortDesc() {
		Mockito.when(listOps.size("usageLog")).thenReturn(21l);
		Mockito.when(listOps.range("usageLog", -29, 20))
				.thenReturn(Arrays.asList(createLog(1), createLog(0), createLog(-1)));
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.DESC);
		assertEquals(2, actualLogs.size());
		assertEquals(1l, actualLogs.get(0).getTime().longValue());
		assertEquals(0l, actualLogs.get(1).getTime().longValue());
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", -29, 20);
	}

	@Test
	public void testFetchLogsByInterval_SortDesc_MoreLogs() {
		Mockito.when(listOps.size("usageLog")).thenReturn(150l);
		Mockito.when(listOps.range("usageLog", 100, 149))
				.thenReturn(Arrays.asList(createLog(1), createLog(0), createLog(-1)));
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.DESC);
		assertEquals(2, actualLogs.size());
		assertEquals(1l, actualLogs.get(0).getTime().longValue());
		assertEquals(0l, actualLogs.get(1).getTime().longValue());
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 100, 149);
	}

	@Test
	public void testFetchLogsByIntervalWithSubsequentFetchDespiteOutOfBoundsTime_SortDesc() {
		Mockito.when(listOps.size("usageLog")).thenReturn(150l);
		Mockito.when(listOps.range("usageLog", 100, 149))
				.thenReturn(Arrays.asList(createLog(2), createLog(0), createLog(1)));
		Mockito.when(listOps.range("usageLog", 50, 99)).thenReturn(Arrays.asList());
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.DESC);
		assertEquals(2, actualLogs.size());
		assertEquals(0l, actualLogs.get(0).getTime().longValue());
		assertEquals(1l, actualLogs.get(1).getTime().longValue());
		Mockito.verify(listOps, Mockito.times(2)).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 100, 149);
		Mockito.verify(listOps).range("usageLog", 50, 99);
	}

	@Test
	public void testFetchLogsByIntervalWithEmptyList_SortDesc() {
		Mockito.when(listOps.size("usageLog")).thenReturn(150l);
		Mockito.when(listOps.range("usageLog", 100, 149)).thenReturn(Arrays.asList());
		List<UsageLog> actualLogs = bar.fetchUsageLogsByInterval(0l, 1l, SortDirection.DESC);
		assertTrue(actualLogs.isEmpty());
		Mockito.verify(listOps).size("usageLog");
		Mockito.verify(listOps).range("usageLog", 100, 149);
	}

	private List<UsageLog> createLogList() {
		return Arrays.asList(new UsageLog("log1"), new UsageLog("log2"));
	}

	private UsageLog createLog(long timeInMillis) {
		UsageLog log = new UsageLog("test");
		log.setTime(timeInMillis);
		return log;
	}
}
