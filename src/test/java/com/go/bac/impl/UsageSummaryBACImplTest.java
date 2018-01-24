package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.go.bar.IUsageLogBAR;
import com.go.bar.IUsageSummaryBAR;
import com.go.model.ModelBuilder;
import com.go.model.SortDirection;
import com.go.model.UsageLog;
import com.go.model.UsageSummary;
import com.go.util.TimeUtil;

public class UsageSummaryBACImplTest {

	@InjectMocks
	private UsageSummaryBACImpl usageSummaryBAC;

	@Mock
	private IUsageLogBAR usageLogBAR;

	@Mock
	private IUsageSummaryBAR usageSummaryBAR;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(usageLogBAR);
		verifyNoMoreInteractions(usageSummaryBAR);
	}

	@Test
	public void testFetchHourlySummaries() {
		List<UsageSummary> expectedSummaries = Arrays.asList(new UsageSummary());
		Mockito.when(usageSummaryBAR.fetchAllHourlySummaries()).thenReturn(expectedSummaries);
		List<UsageSummary> actualSummaries = usageSummaryBAC.fetchAllHourlySummaries();
		Mockito.verify(usageSummaryBAR).fetchAllHourlySummaries();
		assertEquals(expectedSummaries, actualSummaries);
	}

	@Test
	public void testFetchDailySummaries() {
		List<UsageSummary> expectedSummaries = Arrays.asList(new UsageSummary());
		Mockito.when(usageSummaryBAR.fetchAllDailySummaries()).thenReturn(expectedSummaries);
		List<UsageSummary> actualSummaries = usageSummaryBAC.fetchAllDailySummaries();
		Mockito.verify(usageSummaryBAR).fetchAllDailySummaries();
		assertEquals(expectedSummaries, actualSummaries);
	}

	@Test
	public void testFetchTotalSummary() {
		UsageSummary expectedSummary = new UsageSummary();
		Mockito.when(usageSummaryBAR.fetchTotalSummary()).thenReturn(expectedSummary);
		UsageSummary actualSummary = usageSummaryBAC.fetchTotalSummary();
		Mockito.verify(usageSummaryBAR).fetchTotalSummary();
		assertEquals(expectedSummary, actualSummary);
	}

	@Test
	public void testDeleteUsageSummary() {
		List<UsageSummary> summaryList = Arrays.asList(createUsageSummary(1l),
				createUsageSummary(TimeUtil.topOfDay().minusDays(1).plusNanos(1).toInstant().toEpochMilli()));
		Mockito.when(usageSummaryBAR.fetchPagedDailySummary(1, 2)).thenReturn(summaryList);
		Mockito.when(usageSummaryBAR.fetchPagedHourlySummary(1, 25)).thenReturn(summaryList);
		usageSummaryBAC.deleteUsageSummary(1);
		Mockito.verify(usageSummaryBAR).fetchPagedDailySummary(1, 2);
		Mockito.verify(usageSummaryBAR).fetchPagedHourlySummary(1, 25);
		Mockito.verify(usageSummaryBAR).deleteDailySummary(summaryList.get(0));
		Mockito.verify(usageSummaryBAR).deleteHourlySummary(summaryList.get(0));
	}

	@Test
	public void testDeleteUsageSummary_DeleteMoreThanOnePage() {
		List<UsageSummary> summaryList = Arrays.asList(createUsageSummary(1l), createUsageSummary(2l));
		Mockito.when(usageSummaryBAR.fetchPagedDailySummary(1, 2)).thenReturn(summaryList).thenReturn(Arrays.asList());
		Mockito.when(usageSummaryBAR.fetchPagedHourlySummary(1, 25)).thenReturn(summaryList)
				.thenReturn(Arrays.asList());
		usageSummaryBAC.deleteUsageSummary(1);
		Mockito.verify(usageSummaryBAR).fetchPagedDailySummary(1, 2);
		Mockito.verify(usageSummaryBAR).fetchPagedDailySummary(2, 2);
		Mockito.verify(usageSummaryBAR).fetchPagedHourlySummary(1, 25);
		Mockito.verify(usageSummaryBAR).fetchPagedHourlySummary(2, 25);
		Mockito.verify(usageSummaryBAR).deleteDailySummary(summaryList.get(0));
		Mockito.verify(usageSummaryBAR).deleteDailySummary(summaryList.get(1));
		Mockito.verify(usageSummaryBAR).deleteHourlySummary(summaryList.get(0));
		Mockito.verify(usageSummaryBAR).deleteHourlySummary(summaryList.get(1));
	}

	@Test
	public void testDeleteUsageSummary_NothingToDelete() {
		List<UsageSummary> summaryList = Arrays.asList();
		Mockito.when(usageSummaryBAR.fetchPagedDailySummary(1, 2)).thenReturn(summaryList);
		Mockito.when(usageSummaryBAR.fetchPagedHourlySummary(1, 25)).thenReturn(summaryList);
		usageSummaryBAC.deleteUsageSummary(1);
		Mockito.verify(usageSummaryBAR).fetchPagedDailySummary(1, 2);
		Mockito.verify(usageSummaryBAR).fetchPagedHourlySummary(1, 25);
	}

	@Test
	public void testCalculateHourlySummary() {
		long hourBegin = TimeUtil.topOfHour().minusHours(1).toInstant().toEpochMilli();
		long hourEnd = TimeUtil.topOfHour().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(hourBegin, hourEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList(new UsageLog("test")));
		usageSummaryBAC.calculateHourlySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(hourBegin, hourEnd, SortDirection.DESC);
		ArgumentCaptor<UsageSummary> summaryCaptor = ArgumentCaptor.forClass(UsageSummary.class);
		Mockito.verify(usageSummaryBAR).addHourlySummary(summaryCaptor.capture());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().size());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
		assertEquals(hourBegin, summaryCaptor.getValue().getStartTime().longValue());
		assertEquals(1, summaryCaptor.getValue().getTotalCount().intValue());
	}

	@Test
	public void testCalculateHourlySummaryWithMultipleUsageLogs() {
		long hourBegin = TimeUtil.topOfHour().minusHours(1).toInstant().toEpochMilli();
		long hourEnd = TimeUtil.topOfHour().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(hourBegin, hourEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList(new UsageLog("test"), new UsageLog("test1"), new UsageLog("test1")));
		usageSummaryBAC.calculateHourlySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(hourBegin, hourEnd, SortDirection.DESC);
		ArgumentCaptor<UsageSummary> summaryCaptor = ArgumentCaptor.forClass(UsageSummary.class);
		Mockito.verify(usageSummaryBAR).addHourlySummary(summaryCaptor.capture());
		assertEquals(2, summaryCaptor.getValue().getLinkCountMap().size());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
		assertEquals(2, summaryCaptor.getValue().getLinkCountMap().get("test1").intValue());
		assertEquals(hourBegin, summaryCaptor.getValue().getStartTime().longValue());
		assertEquals(3, summaryCaptor.getValue().getTotalCount().intValue());
	}

	@Test
	public void testCalculateDailySummary() {
		long dayBegin = TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
		long dayEnd = TimeUtil.topOfDay().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList(new UsageLog("test")));
		Mockito.when(usageSummaryBAR.fetchAllDailySummaries()).thenReturn(Arrays.asList());
		usageSummaryBAC.calculateDailySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC);
		ArgumentCaptor<UsageSummary> summaryCaptor = ArgumentCaptor.forClass(UsageSummary.class);
		Mockito.verify(usageSummaryBAR).addDailySummary(summaryCaptor.capture());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().size());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
		assertEquals(dayBegin, summaryCaptor.getValue().getStartTime().longValue());
		assertEquals(1, summaryCaptor.getValue().getTotalCount().intValue());
		assertTotalSummary(Long.MAX_VALUE, 0, 0);
	}

	@Test
	public void testCalculateDailySummaryWithMultipleUsageLogs() {
		long dayBegin = TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
		long dayEnd = TimeUtil.topOfDay().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList(new UsageLog("test"), new UsageLog("test1"), new UsageLog("test1")));
		Mockito.when(usageSummaryBAR.fetchAllDailySummaries()).thenReturn(Arrays.asList());
		usageSummaryBAC.calculateDailySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC);
		ArgumentCaptor<UsageSummary> summaryCaptor = ArgumentCaptor.forClass(UsageSummary.class);
		Mockito.verify(usageSummaryBAR).addDailySummary(summaryCaptor.capture());
		assertEquals(2, summaryCaptor.getValue().getLinkCountMap().size());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
		assertEquals(2, summaryCaptor.getValue().getLinkCountMap().get("test1").intValue());
		assertEquals(dayBegin, summaryCaptor.getValue().getStartTime().longValue());
		assertEquals(3, summaryCaptor.getValue().getTotalCount().intValue());
		assertTotalSummary(Long.MAX_VALUE, 0, 0);
	}

	@Test
	public void testCalculateTotalSummary() {
		long dayBegin = TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
		long dayEnd = TimeUtil.topOfDay().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList());
		Mockito.when(usageSummaryBAR.fetchAllDailySummaries()).thenReturn(
				Arrays.asList(ModelBuilder.createSummary(5l, "test", "test"), ModelBuilder.createSummary(4l, "test")));
		usageSummaryBAC.calculateDailySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC);
		Mockito.verify(usageSummaryBAR).addDailySummary(Mockito.any(UsageSummary.class));
		ArgumentCaptor<UsageSummary> summaryCaptor = assertTotalSummary(4l, 1, 3);
		assertEquals(3, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
	}

	@Test
	public void testCalculateTotalSummaryWithMultipleRecords() {
		long dayBegin = TimeUtil.topOfDay().minusDays(1).toInstant().toEpochMilli();
		long dayEnd = TimeUtil.topOfDay().minusNanos(1).toInstant().toEpochMilli();
		Mockito.when(usageLogBAR.fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC))
				.thenReturn(Arrays.asList());
		Mockito.when(usageSummaryBAR.fetchAllDailySummaries()).thenReturn(Arrays.asList(
				ModelBuilder.createSummary(4l, "test", "test"), ModelBuilder.createSummary(5l, "test", "test1")));
		usageSummaryBAC.calculateDailySummary();
		Mockito.verify(usageLogBAR).fetchUsageLogsByInterval(dayBegin, dayEnd, SortDirection.DESC);
		Mockito.verify(usageSummaryBAR).addDailySummary(Mockito.any(UsageSummary.class));
		ArgumentCaptor<UsageSummary> summaryCaptor = assertTotalSummary(4l, 2, 4);
		assertEquals(3, summaryCaptor.getValue().getLinkCountMap().get("test").intValue());
		assertEquals(1, summaryCaptor.getValue().getLinkCountMap().get("test1").intValue());
	}

	private ArgumentCaptor<UsageSummary> assertTotalSummary(long startTime, int numUniqueLinks, int totalCount) {
		ArgumentCaptor<UsageSummary> summaryCaptor = ArgumentCaptor.forClass(UsageSummary.class);
		Mockito.verify(usageSummaryBAR).fetchAllDailySummaries();
		Mockito.verify(usageSummaryBAR).setTotalSummary(summaryCaptor.capture());
		assertEquals(startTime, summaryCaptor.getValue().getStartTime().longValue());
		assertEquals(numUniqueLinks, summaryCaptor.getValue().getLinkCountMap().size());
		assertEquals(totalCount, summaryCaptor.getValue().getTotalCount().intValue());
		return summaryCaptor;
	}

	private UsageSummary createUsageSummary(Long startTime) {
		UsageSummary summary = new UsageSummary();
		summary.setStartTime(startTime);
		summary.setTotalCount(1);
		return summary;
	}

}
