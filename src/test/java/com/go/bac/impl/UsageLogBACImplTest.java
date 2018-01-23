package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

}
