package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.go.bac.impl.LogBACImpl;
import com.go.bar.ILogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.UsageLog;

public class LogBACImplTest {

	@InjectMocks
	private LogBACImpl logBAC;

	@Mock
	private ILogBAR logBAR;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(logBAR);
	}

	@Test
	public void testCreateLog() {
		long currentTimeInMillis = System.currentTimeMillis();
		logBAC.logLinkCreation("test@test.com", "test");
		ArgumentCaptor<UsageLog> usageCaptor = ArgumentCaptor.forClass(UsageLog.class);
		Mockito.verify(logBAR).log(usageCaptor.capture());
		assertEquals("test@test.com", usageCaptor.getValue().getUsername());
		assertEquals("test", usageCaptor.getValue().getLinkName());
		assertTrue(usageCaptor.getValue().getTime() >= currentTimeInMillis);
		assertEquals(ActionTypeEnum.CREATE, usageCaptor.getValue().getAction());
	}

	@Test
	public void testDeleteLog() {
		long currentTimeInMillis = System.currentTimeMillis();
		logBAC.logLinkDeletion("test@test.com", "test");
		ArgumentCaptor<UsageLog> usageCaptor = ArgumentCaptor.forClass(UsageLog.class);
		Mockito.verify(logBAR).log(usageCaptor.capture());
		assertEquals("test@test.com", usageCaptor.getValue().getUsername());
		assertEquals("test", usageCaptor.getValue().getLinkName());
		assertTrue(usageCaptor.getValue().getTime() >= currentTimeInMillis);
		assertEquals(ActionTypeEnum.DELETE, usageCaptor.getValue().getAction());
	}

	@Test
	public void testGetAllLogs() {
		List<UsageLog> expectedLogs = new ArrayList<>();
		Mockito.when(logBAR.getAllLogs()).thenReturn(expectedLogs);
		List<UsageLog> actualLogs = logBAC.fetchAllLogs();
		assertEquals(expectedLogs, actualLogs);
		Mockito.verify(logBAR).getAllLogs();
	}

	@Test
	public void testGetUserLogs() {
		Mockito.when(logBAR.getAllLogs()).thenReturn(createLogs());
		List<UsageLog> logs = logBAC.fetchLogsForUser("test@test.com");
		assertEquals(2, logs.size());
		assertEquals("test@test.com", logs.get(0).getUsername());
		assertEquals("test@test.com", logs.get(1).getUsername());
		Mockito.verify(logBAR).getAllLogs();
	}

	private List<UsageLog> createLogs() {
		List<UsageLog> logs = new ArrayList<>();
		logs.add(createLog("nobody@test.com"));
		logs.add(createLog("test@test.com"));
		logs.add(createLog("somebody@test.com"));
		logs.add(createLog("test@test.com"));
		return logs;
	}

	private UsageLog createLog(String username) {
		UsageLog log = new UsageLog();
		log.setUsername(username);
		return log;
	}

}
