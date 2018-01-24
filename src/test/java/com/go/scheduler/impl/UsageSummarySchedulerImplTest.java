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

import com.go.bac.IUsageSummaryBAC;

public class UsageSummarySchedulerImplTest {

	@InjectMocks
	private UsageSummarySchedulerImpl usageSummaryScheduler;

	@Mock
	private IUsageSummaryBAC usageSummaryBAC;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(usageSummaryBAC);
	}

	@Test
	public void testScheduledUsageSummaryDelete() {
		ReflectionTestUtils.setField(usageSummaryScheduler, "usageSummaryDaysToKeep", 30);
		usageSummaryScheduler.deleteUsageSummary();
		Mockito.verify(usageSummaryBAC).deleteUsageSummary(30);
	}

	@Test
	public void testScheduledHourlySummary() {
		usageSummaryScheduler.calculateHourlySummary();
		Mockito.verify(usageSummaryBAC).calculateHourlySummary();
	}

	@Test
	public void testScheduledDailySummary() {
		usageSummaryScheduler.calculateDailySummary();
		Mockito.verify(usageSummaryBAC).calculateDailySummary();
	}

}
