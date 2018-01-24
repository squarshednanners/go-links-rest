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

import com.go.bac.IUsageSummaryBAC;
import com.go.exception.GoLinkException;
import com.go.model.Response;
import com.go.model.UsageSummary;

public class UsageSummaryControllerTest {

	@InjectMocks
	private UsageSummaryController summaryController;

	@Mock
	private IUsageSummaryBAC summaryBAC;

	@Mock
	private MessageSourceAccessor messageSourceAccessor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(summaryBAC);
		verifyNoMoreInteractions(messageSourceAccessor);
	}

	@Test
	public void testGetTotalSummary() {
		UsageSummary summary = new UsageSummary();
		when(summaryBAC.fetchTotalSummary()).thenReturn(summary);
		Response<UsageSummary> response = summaryController.fetchTotalSummary();
		assertEquals(summary, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(summaryBAC).fetchTotalSummary();
	}

	@Test
	public void testGetTotalSummaryOnException() {
		when(summaryBAC.fetchTotalSummary()).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<UsageSummary> response = summaryController.fetchTotalSummary();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(summaryBAC).fetchTotalSummary();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetTotalSummaryOnGoLinkException() {
		when(summaryBAC.fetchTotalSummary()).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<UsageSummary> response = summaryController.fetchTotalSummary();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(summaryBAC).fetchTotalSummary();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testGetHourlySummaries() {
		List<UsageSummary> summary = Arrays.asList(new UsageSummary());
		when(summaryBAC.fetchAllHourlySummaries()).thenReturn(summary);
		Response<List<UsageSummary>> response = summaryController.fetchHourlySummaries();
		assertEquals(summary, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(summaryBAC).fetchAllHourlySummaries();
	}

	@Test
	public void testGetHourlySummariesOnException() {
		when(summaryBAC.fetchAllHourlySummaries()).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<List<UsageSummary>> response = summaryController.fetchHourlySummaries();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(summaryBAC).fetchAllHourlySummaries();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetHourlySummariesOnGoLinkException() {
		when(summaryBAC.fetchAllHourlySummaries()).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<UsageSummary>> response = summaryController.fetchHourlySummaries();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(summaryBAC).fetchAllHourlySummaries();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testGetDailySummaries() {
		List<UsageSummary> summary = Arrays.asList(new UsageSummary());
		when(summaryBAC.fetchAllDailySummaries()).thenReturn(summary);
		Response<List<UsageSummary>> response = summaryController.fetchDailySummaries();
		assertEquals(summary, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(summaryBAC).fetchAllDailySummaries();
	}

	@Test
	public void testGetDailySummariesOnException() {
		when(summaryBAC.fetchAllDailySummaries()).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<List<UsageSummary>> response = summaryController.fetchDailySummaries();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(summaryBAC).fetchAllDailySummaries();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetDailySummariesOnGoLinkException() {
		when(summaryBAC.fetchAllDailySummaries()).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("summary.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<UsageSummary>> response = summaryController.fetchDailySummaries();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(summaryBAC).fetchAllDailySummaries();
		verify(messageSourceAccessor).getMessage(eq("summary.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

}
