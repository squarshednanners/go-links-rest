package com.go.scheduler.impl;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.go.bac.IUsageLogBAC;

public class UsageLogSchedulerImplTest {

	@InjectMocks
	private UsageLogSchedulerImpl usageLogScheduler;

	@Mock
	private IUsageLogBAC usageLogBAC;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(usageLogBAC);
	}

	@Test
	public void testScheduledUsageLogDelete() {
		ReflectionTestUtils.setField(usageLogScheduler, "usageLogDaysToKeep", 15);
		usageLogScheduler.deleteUsageLogs();
		Mockito.verify(usageLogBAC).deleteUsageLogs(15);
	}

}
