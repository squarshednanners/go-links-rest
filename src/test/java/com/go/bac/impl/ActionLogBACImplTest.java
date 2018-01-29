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

import com.go.bac.impl.ActionLogBACImpl;
import com.go.bar.IActionLogBAR;
import com.go.model.ActionTypeEnum;
import com.go.model.SortDirection;
import com.go.model.ActionLog;

public class ActionLogBACImplTest {

	@InjectMocks
	private ActionLogBACImpl actionLogBAC;

	@Mock
	private IActionLogBAR actionLogBAR;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(actionLogBAR);
	}

	@Test
	public void testCreateLog() {
		long currentTimeInMillis = System.currentTimeMillis();
		actionLogBAC.logLinkCreation("test@test.com", "test");
		ArgumentCaptor<ActionLog> actionCaptor = ArgumentCaptor.forClass(ActionLog.class);
		Mockito.verify(actionLogBAR).log(actionCaptor.capture());
		assertEquals("test@test.com", actionCaptor.getValue().getUsername());
		assertEquals("test", actionCaptor.getValue().getLinkName());
		assertTrue(actionCaptor.getValue().getTime() >= currentTimeInMillis);
		assertEquals(ActionTypeEnum.CREATE, actionCaptor.getValue().getAction());
	}

	@Test
	public void testDeleteLog() {
		long currentTimeInMillis = System.currentTimeMillis();
		actionLogBAC.logLinkDeletion("test@test.com", "test");
		ArgumentCaptor<ActionLog> actionCaptor = ArgumentCaptor.forClass(ActionLog.class);
		Mockito.verify(actionLogBAR).log(actionCaptor.capture());
		assertEquals("test@test.com", actionCaptor.getValue().getUsername());
		assertEquals("test", actionCaptor.getValue().getLinkName());
		assertTrue(actionCaptor.getValue().getTime() >= currentTimeInMillis);
		assertEquals(ActionTypeEnum.DELETE, actionCaptor.getValue().getAction());
	}

	@Test
	public void testGetAllLogs() {
		List<ActionLog> expectedLogs = new ArrayList<>();
		Mockito.when(actionLogBAR.getAllLogs(SortDirection.DESC)).thenReturn(expectedLogs);
		List<ActionLog> actualLogs = actionLogBAC.fetchAllLogs(SortDirection.DESC);
		assertEquals(expectedLogs, actualLogs);
		Mockito.verify(actionLogBAR).getAllLogs(SortDirection.DESC);
	}

	@Test
	public void testGetUserLogs() {
		Mockito.when(actionLogBAR.getAllLogs(null)).thenReturn(createLogs());
		List<ActionLog> logs = actionLogBAC.fetchLogsForUser("test@test.com");
		assertEquals(2, logs.size());
		assertEquals("test@test.com", logs.get(0).getUsername());
		assertEquals("test@test.com", logs.get(1).getUsername());
		Mockito.verify(actionLogBAR).getAllLogs(null);
	}

	private List<ActionLog> createLogs() {
		List<ActionLog> logs = new ArrayList<>();
		logs.add(createLog("nobody@test.com"));
		logs.add(createLog("test@test.com"));
		logs.add(createLog("somebody@test.com"));
		logs.add(createLog("test@test.com"));
		return logs;
	}

	private ActionLog createLog(String username) {
		ActionLog log = new ActionLog();
		log.setUsername(username);
		return log;
	}

}
