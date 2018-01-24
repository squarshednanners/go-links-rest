package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.go.bar.IUsageLogBAR;
import com.go.model.ActionLog;
import com.go.model.UsageLog;
import com.go.model.SortDirection;
import com.go.util.TimeUtil;

public class UsageLogBACImplTest {

	@InjectMocks
	private UsageLogBACImpl usageLogBAC;

	@Mock
	private IUsageLogBAR usageLogBAR;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(usageLogBAR);
	}

	@Test
	public void testCreateLog() {
		long currentTimeInMillis = System.currentTimeMillis();
		usageLogBAC.logUsage("test");
		ArgumentCaptor<UsageLog> actionCaptor = ArgumentCaptor.forClass(ActionLog.class);
		Mockito.verify(usageLogBAR).log(actionCaptor.capture());
		assertEquals("test", actionCaptor.getValue().getLinkName());
		assertTrue(actionCaptor.getValue().getTime() >= currentTimeInMillis);
	}

	@Test
	public void testDeleteUsageLogsWhenNothingToDelete() {
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(0l, midnightYesterday(), SortDirection.ASC))
				.thenReturn(Lists.emptyList());
		usageLogBAC.deleteUsageLogs(1);
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(0l, midnightYesterday(), SortDirection.ASC);
	}

	@Test
	public void testDeleteOneUsageLog() {
		UsageLog log1 = new UsageLog();
		UsageLog log2 = new UsageLog();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(0l, midnightYesterday(), SortDirection.ASC))
				.thenReturn(Arrays.asList(log1, log2));
		usageLogBAC.deleteUsageLogs(1);
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(0l, midnightYesterday(), SortDirection.ASC);
		Mockito.verify(usageLogBAR).deleteLog(log1);
		Mockito.verify(usageLogBAR).deleteLog(log2);

	}

	private long midnightYesterday() {
		return TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
	}

}
