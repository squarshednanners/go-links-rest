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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.util.ReflectionTestUtils;

import com.go.bac.IGoBAC;
import com.go.controller.GoController;
import com.go.exception.GoLinkException;
import com.go.model.GoLink;
import com.go.model.Response;

public class GoControllerTest {

	@InjectMocks
	private GoController goController;

	@Mock
	private IGoBAC goBAC;

	@Mock
	private MessageSourceAccessor messageSourceAccessor;

	@Mock
	private Principal principal;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(principal.getName()).thenReturn("principal@test.com");
	}

	@After
	public void verifyMocks() {
		verifyNoMoreInteractions(goBAC);
		verifyNoMoreInteractions(messageSourceAccessor);
		verifyNoMoreInteractions(principal);
	}

	@Test
	public void testGetAllLinks() {
		List<GoLink> linkList = new ArrayList<>();
		when(goBAC.fetchAllLinks()).thenReturn(linkList);
		Response<List<GoLink>> response = goController.fetchAllLinks();
		assertEquals(linkList, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(goBAC).fetchAllLinks();
	}

	@Test
	public void testGetAllLinksOnException() {
		when(goBAC.fetchAllLinks()).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<List<GoLink>> response = goController.fetchAllLinks();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).fetchAllLinks();
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGetAllLinksOnGoLinkException() {
		when(goBAC.fetchAllLinks()).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<GoLink>> response = goController.fetchAllLinks();
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(goBAC).fetchAllLinks();
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testSearchLinks() {
		List<GoLink> linkList = new ArrayList<>();
		when(goBAC.searchLinks("stuff")).thenReturn(linkList);
		Response<List<GoLink>> response = goController.searchLinks("stuff");
		assertEquals(linkList, response.getResults());
		assertTrue(response.getSuccessful());
		assertTrue(response.getMessageList().isEmpty());
		verify(goBAC).searchLinks("stuff");
	}

	@Test
	public void testSearchLinksOnException() {
		when(goBAC.searchLinks("stuff")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<List<GoLink>> response = goController.searchLinks("stuff");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).searchLinks("stuff");
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
	}

	@Test
	public void testSearchLinksOnGoLinkException() {
		when(goBAC.searchLinks("stuff")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<List<GoLink>> response = goController.searchLinks("stuff");
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(goBAC).searchLinks("stuff");
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testCreateLink() {
		GoLink createLink = createLink();
		when(goBAC.createLink(createLink, "principal@test.com")).thenReturn(createLink);
		when(messageSourceAccessor.getMessage(eq("go.link.created"), any(Object[].class))).thenReturn("parsed message");
		Response<GoLink> response = goController.createLink(createLink, principal);
		assertEquals(createLink, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(goBAC).createLink(createLink, "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.created"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testCreateLinksOnException() {
		GoLink createLink = createLink();
		when(goBAC.createLink(createLink, "principal@test.com")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.create.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<GoLink> response = goController.createLink(createLink, principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).createLink(createLink, "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.create.error"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testCreateLinksOnGoLinkException() {
		GoLink createLink = createLink();
		when(goBAC.createLink(createLink, "principal@test.com")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("go.link.create.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<GoLink> response = goController.createLink(createLink, principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(goBAC).createLink(createLink, "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.create.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
		verify(principal).getName();
	}

	@Test
	public void testDeleteLink() {
		GoLink deleteLink = createLink();
		when(goBAC.deleteLink("test", "principal@test.com")).thenReturn(deleteLink);
		when(messageSourceAccessor.getMessage(eq("go.link.deleted"), any(Object[].class))).thenReturn("parsed message");
		Response<GoLink> response = goController.deleteLink("test", principal);
		assertEquals(deleteLink, response.getResults());
		assertTrue(response.getSuccessful());
		assertEquals(1, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		verify(goBAC).deleteLink("test", "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.deleted"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testDeleteLinksOnException() {
		when(goBAC.deleteLink("test", "principal@test.com")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.delete.error"), any(Object[].class)))
				.thenReturn("parsed message");
		Response<GoLink> response = goController.deleteLink("test", principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).deleteLink("test", "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.delete.error"), any(Object[].class));
		verify(principal).getName();
	}

	@Test
	public void testDeleteLinksOnGoLinkException() {
		when(goBAC.deleteLink("test", "principal@test.com")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("go.link.delete.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		Response<GoLink> response = goController.deleteLink("test", principal);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(goBAC).deleteLink("test", "principal@test.com");
		verify(messageSourceAccessor).getMessage(eq("go.link.delete.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
		verify(principal).getName();
	}

	@Test
	public void testGo() {
		GoLink goLink = createLink();
		when(goBAC.fetchLink("test")).thenReturn(goLink);
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", httpResponse);
		assertNull(response);
		verify(goBAC).fetchLink("test");
		verify(httpResponse).setStatus(302);
		verify(httpResponse).setHeader("Location", "http://test.com");
		verifyNoMoreInteractions(httpResponse);
	}

	@Test
	public void testGoOnException() {
		when(goBAC.fetchLink("test")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", httpResponse);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).fetchLink("test");
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGoWithParam() {
		GoLink goLink = createLink();
		when(goBAC.fetchLink("test")).thenReturn(goLink);
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", "1", httpResponse);
		assertNull(response);
		verify(goBAC).fetchLink("test");
		verify(httpResponse).setStatus(302);
		verify(httpResponse).setHeader("Location", "http://test.com");
		verifyNoMoreInteractions(httpResponse);
	}

	@Test
	public void testGoWithParamOnException() {
		when(goBAC.fetchLink("test")).thenThrow(new RuntimeException("Bad things happened"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", "1", httpResponse);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("Bad things happened", response.getMessageList().get(1));
		verify(goBAC).fetchLink("test");
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
	}

	@Test
	public void testGoOnGoLinkException() {
		when(goBAC.fetchLink("test")).thenThrow(new GoLinkException("error.code", "arg1"));
		when(messageSourceAccessor.getMessage(eq("go.link.fetch.error"), any(Object[].class)))
				.thenReturn("parsed message");
		when(messageSourceAccessor.getMessage(eq("error.code"), any(Object[].class))).thenReturn("parsed message 2");
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", null, httpResponse);
		assertNull(response.getResults());
		assertFalse(response.getSuccessful());
		assertEquals(2, response.getMessageList().size());
		assertEquals("parsed message", response.getMessageList().get(0));
		assertEquals("parsed message 2", response.getMessageList().get(1));
		verify(goBAC).fetchLink("test");
		verify(messageSourceAccessor).getMessage(eq("go.link.fetch.error"), any(Object[].class));
		ArgumentCaptor<Object[]> argCaptor = ArgumentCaptor.forClass(Object[].class);
		verify(messageSourceAccessor).getMessage(eq("error.code"), argCaptor.capture());
		assertEquals(1, argCaptor.getValue().length);
		assertEquals("arg1", argCaptor.getValue()[0]);
	}

	@Test
	public void testGoWhenNotExistsRedirectToCreatePage() {
		ReflectionTestUtils.setField(goController, "linkNotExistsUrl", "http://localhost:8081/#/createLink?name=test&nogo=true");
		when(goBAC.fetchLink("test")).thenThrow(new GoLinkException("go.link.does.not.exist"));
		HttpServletResponse httpResponse = Mockito.mock(HttpServletResponse.class);
		Response<GoLink> response = goController.go("test", httpResponse);
		assertNull(response);
		verify(goBAC).fetchLink("test");
		verify(httpResponse).setStatus(302);
		verify(httpResponse).setHeader("Location", "http://localhost:8081/#/createLink?name=test&nogo=true");
		verifyNoMoreInteractions(httpResponse);
	}

	private GoLink createLink() {
		GoLink link = new GoLink();
		link.setName("test");
		link.setUrl("http://test.com");
		return link;
	}

}
