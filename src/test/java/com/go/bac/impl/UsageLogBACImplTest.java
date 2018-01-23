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
import com.go.model.Log;

public class UsageLogBACImplTest {

	private static final long MILLIS_IN_DAY = 86400001;

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
		ArgumentCaptor<Log> actionCaptor = ArgumentCaptor.forClass(ActionLog.class);
		Mockito.verify(usageLogBAR).log(actionCaptor.capture());
		assertEquals("test", actionCaptor.getValue().getLinkName());
		assertTrue(actionCaptor.getValue().getTime() >= currentTimeInMillis);
	}

	@Test
	public void testDeleteUsageLogsWhenNothingToDelete() {
		Mockito.when(usageLogBAR.getOldestUsageLogs(1, 100)).thenReturn(Lists.emptyList());
		usageLogBAC.deleteUsageLogs(1);
		Mockito.verify(usageLogBAR).getOldestUsageLogs(1, 100);
	}

	@Test
	public void testDeleteOneUsageLog() {
		Log log1 = createLog(true);
		Mockito.when(usageLogBAR.getOldestUsageLogs(1, 100)).thenReturn(Arrays.asList(log1, createLog(false)));
		usageLogBAC.deleteUsageLogs(1);
		Mockito.verify(usageLogBAR).getOldestUsageLogs(1, 100);
		Mockito.verify(usageLogBAR).deleteLog(log1);
	}

	@Test
	public void testDeleteMultipleSetsOfUsageLogs() {
		Mockito.when(usageLogBAR.getOldestUsageLogs(1, 100)).thenReturn(Arrays.asList(createLog(true), createLog(true)))
				.thenReturn(Arrays.asList(createLog(true), createLog(false)));
		usageLogBAC.deleteUsageLogs(1);
		Mockito.verify(usageLogBAR, Mockito.times(2)).getOldestUsageLogs(1, 100);
		Mockito.verify(usageLogBAR, Mockito.times(3)).deleteLog(Mockito.any(Log.class));
	}

	private Log createLog(boolean subtractDay) {
		Log log = new Log("test");
		if (subtractDay) {
			log.setTime(System.currentTimeMillis() - MILLIS_IN_DAY);
		} else {
			log.setTime(System.currentTimeMillis() - MILLIS_IN_DAY + 2);
		}
		return log;
	}

}
