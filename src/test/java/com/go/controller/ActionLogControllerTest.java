package com.go.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.MessageSourceAccessor;

import com.go.bac.IActionLogBAC;
import com.go.exception.GoLinkException;
import com.go.model.ActionLog;
import com.go.model.Response;
import com.go.model.SortDirection;

public class ActionLogControllerTest {

	@InjectMocks
	private ActionLogController logController;

	@Mock
	private IActionLogBAC logBAC;

	@Mock
	private MessageSourceAccessor messageSourceAccessor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(logBAC);
		verifyNoMoreInteractions(messageSourceAccessor);
	}

	@Test
	public void testGetAllLogs() {
		List<ActionLog> logList = Arrays.asList(new ActionLog());
		when(logBAC.fetchAllLogs(SortDirection.DESC)).thenReturn(logList);
		Response<List<ActionLog>> logResponse = logController.fetchAllLogs(SortDirection.DESC);
		assertEquals(logList, logResponse.getResults());
		assertTrue(logResponse.getSuccessful());
		assertTrue(logResponse.getMessageList().isEmpty());
		verify(logBAC).fetchAllLogs(SortDirection.DESC);
	}

	@Test
	public void testGetAllLogsOnException() {
		when(logBAC.fetchAllLogs(null)).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("log.fetch.error"), any(Object[].class))).thenReturn("parsed message");
		Response<List<ActionLog>> logResponse = logController.fetchAllLogs(null);
		assertNull(logResponse.getResults());
		assertFalse(logResponse.getSuccessful());
		assertEquals(2, logResponse.getMessageList().size());
		assertEquals("parsed message", logResponse.getMessageList().get(0));
		assertEquals("Bad things happened", logResponse.getMessageList().get(1));
		verify(logBAC).fetchAllLogs(null);
		verify(messageSourceAccessor).getMessage(eq("log.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetAllLogsOnGoLinkException() {
		when(logBAC.fetchAllLogs(null)).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("log.fetch.error"), any(Object[].class))).thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<ActionLog>> logResponse = logController.fetchAllLogs(null);
		assertNull(logResponse.getResults());
		assertFalse(logResponse.getSuccessful());
		assertEquals(2, logResponse.getMessageList().size());
		assertEquals("parsed message", logResponse.getMessageList().get(0));
		assertEquals("parsed message 2", logResponse.getMessageList().get(1));
		verify(logBAC).fetchAllLogs(null);
		verify(messageSourceAccessor).getMessage(eq("log.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testGetUserLogs() {
		List<ActionLog> logList = Arrays.asList(new ActionLog());
		when(logBAC.fetchLogsForUser("test@test.com")).thenReturn(logList);
		Response<List<ActionLog>> logResponse = logController.fetchUserLogs("test@test.com");
		assertEquals(logList, logResponse.getResults());
		assertTrue(logResponse.getSuccessful());
		assertTrue(logResponse.getMessageList().isEmpty());
		verify(logBAC).fetchLogsForUser("test@test.com");
	}

	@Test
	public void testGetUserLogsOnException() {
		when(logBAC.fetchLogsForUser("test@test.com")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("log.fetch.error"), any(Object[].class))).thenReturn("parsed message");
		Response<List<ActionLog>> logResponse = logController.fetchUserLogs("test@test.com");
		assertNull(logResponse.getResults());
		assertFalse(logResponse.getSuccessful());
		assertEquals(2, logResponse.getMessageList().size());
		assertEquals("parsed message", logResponse.getMessageList().get(0));
		assertEquals("Bad things happened", logResponse.getMessageList().get(1));
		verify(logBAC).fetchLogsForUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("log.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetUserLogsOnGoLinkException() {
		when(logBAC.fetchLogsForUser("test@test.com")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("log.fetch.error"), any(Object[].class))).thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<ActionLog>> logResponse = logController.fetchUserLogs("test@test.com");
		assertNull(logResponse.getResults());
		assertFalse(logResponse.getSuccessful());
		assertEquals(2, logResponse.getMessageList().size());
		assertEquals("parsed message", logResponse.getMessageList().get(0));
		assertEquals("parsed message 2", logResponse.getMessageList().get(1));
		verify(logBAC).fetchLogsForUser("test@test.com");
		verify(messageSourceAccessor).getMessage(eq("log.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}
}
