package com.go.bar.redis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.go.model.UsageSummary;

public class RedisUsageSummaryBARTest {

	@InjectMocks
	private RedisUsageSummaryBAR bar;

	@Mock
	private RedisTemplate<String, UsageSummary> usageSummaryRedisTemplate;

	@Mock
	private ListOperations<String, UsageSummary> listOps;

	@Mock
	private ValueOperations<String, UsageSummary> valueOps;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		Mockito.verifyNoMoreInteractions(usageSummaryRedisTemplate);
		Mockito.verifyNoMoreInteractions(listOps);
		Mockito.verifyNoMoreInteractions(valueOps);
	}

	@Test
	public void testFetchHourlySummary() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("hourlyUsageSummary", 0, -1)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchAllHourlySummaries();
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("hourlyUsageSummary", 0, -1);
	}

	@Test
	public void testFetchHourlySummaryPaged() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("hourlyUsageSummary", 0, 1)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedHourlySummary(1, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("hourlyUsageSummary", 0, 1);
	}

	@Test
	public void testFetchHourlySummaryPaged_SubsequentPage() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("hourlyUsageSummary", 2, 3)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedHourlySummary(2, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("hourlyUsageSummary", 2, 3);
	}

	@Test
	public void testFetchHourlySummaryPaged_TenthPage() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("hourlyUsageSummary", 18, 19)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedHourlySummary(10, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("hourlyUsageSummary", 18, 19);
	}

	@Test
	public void testCreateHourlySummary() {
		UsageSummary summary = new UsageSummary();
		bar.addHourlySummary(summary);
		Mockito.verify(listOps).rightPush("hourlyUsageSummary", summary);
	}

	@Test
	public void testDeleteHourlySummary() {
		UsageSummary summary = new UsageSummary();
		bar.deleteHourlySummary(summary);
		Mockito.verify(listOps).remove("hourlyUsageSummary", 1, summary);
	}

	@Test
	public void testFetchDailySummary() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("dailyUsageSummary", 0, -1)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchAllDailySummaries();
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("dailyUsageSummary", 0, -1);
	}

	@Test
	public void testFetchDailySummaryPaged() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("dailyUsageSummary", 0, 1)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedDailySummary(1, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("dailyUsageSummary", 0, 1);
	}

	@Test
	public void testFetchDailySummaryPaged_SubsequentPage() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("dailyUsageSummary", 2, 3)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedDailySummary(2, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("dailyUsageSummary", 2, 3);
	}

	@Test
	public void testFetchDailySummaryPaged_TenthPage() {
		List<UsageSummary> expectedList = createUsageSummaryList();
		Mockito.when(listOps.range("dailyUsageSummary", 18, 19)).thenReturn(expectedList);
		List<UsageSummary> actualList = bar.fetchPagedDailySummary(10, 2);
		assertEquals(expectedList, actualList);
		Mockito.verify(listOps).range("dailyUsageSummary", 18, 19);
	}

	@Test
	public void testCreateDailySummary() {
		UsageSummary summary = new UsageSummary();
		bar.addDailySummary(summary);
		Mockito.verify(listOps).rightPush("dailyUsageSummary", summary);
	}

	@Test
	public void testDeleteDailySummary() {
		UsageSummary summary = new UsageSummary();
		bar.deleteDailySummary(summary);
		Mockito.verify(listOps).remove("dailyUsageSummary", 1, summary);
	}

	@Test
	public void testFetchTotalSummary() {
		UsageSummary expectedSummary = new UsageSummary();
		Mockito.when(valueOps.get("totalUsageSummary")).thenReturn(expectedSummary);
		UsageSummary actualSummary = bar.fetchTotalSummary();
		Mockito.verify(valueOps).get("totalUsageSummary");
		assertEquals(expectedSummary, actualSummary);
	}

	@Test
	public void testSetTotalSummary() {
		UsageSummary summary = new UsageSummary();
		bar.setTotalSummary(summary);
		Mockito.verify(valueOps).set("totalUsageSummary", summary);
	}
	
	List<UsageSummary> createUsageSummaryList() {
		return Arrays.asList(new UsageSummary());
	}

}
