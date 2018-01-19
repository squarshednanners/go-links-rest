package com.go.bac.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.go.bac.ILogBAC;
import com.go.bac.impl.GoBACImpl;
import com.go.bar.IGoLinkBAR;
import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.GoLinkParameter;
import com.go.validation.GoLinkValidator;

public class GoBACImplTest {

	@InjectMocks
	private GoBACImpl goBAC;

	@Mock
	private IGoLinkBAR goBAR;

	@Mock
	private ILogBAC logBAC;

	@Mock
	private GoLinkValidator goLinkValidator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(goLinkValidator);
		verifyNoMoreInteractions(logBAC);
		verifyNoMoreInteractions(goBAR);
	}

	@Test
	public void testGetAllLinks() {
		List<GoLink> expectedLinkList = new ArrayList<>();
		when(goBAR.getAllLinks()).thenReturn(expectedLinkList);
		List<GoLink> linkList = goBAC.fetchAllLinks();
		assertEquals(expectedLinkList, linkList);
		verify(goBAR).getAllLinks();
	}

	@Test
	public void testGetLink() {
		GoLink expectedLink = createLink();
		when(goBAR.getLink("test")).thenReturn(expectedLink);
		GoLink link = goBAC.fetchLink("test");
		assertEquals(expectedLink, link);
		verify(goLinkValidator).validateName("test");
		verify(goBAR).getLink("test");
	}

	@Test
	public void testGetLinkWhenNoLinkExists() {
		when(goBAR.getLink("test")).thenReturn(null);
		try {
			goBAC.fetchLink("test");
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
			verify(goLinkValidator).validateName("test");
			verify(goBAR).getLink("test");
		}
	}

	@Test
	public void testCreateLink() {
		when(goBAR.getLink("test")).thenReturn(null);
		GoLink createLink = createLink();
		GoLink actualLink = goBAC.createLink(createLink, "test@test.com");
		assertEquals(createLink, actualLink);
		verify(goLinkValidator).validateLink(createLink);
		verify(goBAR).getLink("test");
		verify(goBAR).createLink(createLink);
		verify(logBAC).logLinkCreation("test@test.com", "test");
	}

	@Test
	public void testCreateLinkWithParametersDefaulted() {
		when(goBAR.getLink("test")).thenReturn(null);
		GoLink createLink = createLink();
		createLink.setParameter(new GoLinkParameter());
		GoLink actualLink = goBAC.createLink(createLink, "test@test.com");
		assertEquals(createLink, actualLink);
		assertNull(actualLink.getParameter());
		verify(goLinkValidator).validateLink(createLink);
		verify(goBAR).getLink("test");
		verify(goBAR).createLink(createLink);
		verify(logBAC).logLinkCreation("test@test.com", "test");
	}

	@Test
	public void testCreateLinkWhenAlreadyExists() {
		GoLink createLink = createLink();
		GoLink existingLink = createLink();
		when(goBAR.getLink("test")).thenReturn(existingLink);
		try {
			goBAC.createLink(createLink, "test@test.com");
		} catch (GoLinkException ge) {
			assertEquals("go.link.already.exists", ge.getErrorMessage().getCode());
			assertEquals("TEST", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
			verify(goLinkValidator).validateLink(createLink);
			verify(goBAR).getLink("test");
		}
	}

	@Test
	public void testDeleteLink() {
		GoLink existingLink = createLink();
		when(goBAR.getLink("test")).thenReturn(existingLink);
		GoLink actualLink = goBAC.deleteLink("test", "test@test.com");
		assertEquals(existingLink, actualLink);
		verify(goLinkValidator).validateName("test");
		verify(goBAR).getLink("test");
		verify(goBAR).deleteLink("TEST");
		verify(logBAC).logLinkDeletion("test@test.com", "TEST");
	}

	@Test
	public void testDeleteLinkWhenNotExists() {
		when(goBAR.getLink("test")).thenReturn(null);
		try {
			goBAC.deleteLink("test", "test@test.com");
			fail();
		} catch (GoLinkException ge) {
			assertEquals("go.link.does.not.exist", ge.getErrorMessage().getCode());
			assertEquals("test", ge.getErrorMessage().getArgs()[0]);
			assertEquals(1, ge.getErrorMessage().getArgs().length);
			verify(goLinkValidator).validateName("test");
			verify(goBAR).getLink("test");
		}

	}

	@Test
	public void testSearchLinks() {
		when(goBAR.getAllLinks()).thenReturn(createLinkList());
		List<GoLink> actualLinkList = goBAC.searchLinks("stuff");
		assertEquals(2, actualLinkList.size());
		assertEquals("thestufflink", actualLinkList.get(0).getName());
		assertEquals("somethingelse", actualLinkList.get(1).getName());
		verify(goBAR).getAllLinks();
	}

	private List<GoLink> createLinkList() {
		return Arrays.asList(createLink("thestufflink", "http://google.com"),
				createLink("nothingToSeeHere", "http://nothing.com"), createLink("somethingelse", "http://stuff.com"));
	}

	private GoLink createLink() {
		return createLink("TEST", "http://www.test.com");
	}

	private GoLink createLink(String linkName, String url) {
		GoLink link = new GoLink();
		link.setName(linkName);
		link.setUrl(url);
		return link;
	}

}
