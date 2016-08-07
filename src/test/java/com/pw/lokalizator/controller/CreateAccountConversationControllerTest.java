package com.pw.lokalizator.controller;

import java.io.IOException;


import javax.enterprise.context.Conversation;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountConversationControllerTest {
	@Mock
	private Conversation conversation;
	@Mock
	private UserGoogleMapController googleMapController;
	@Mock
	private UserRepository userRepository;
	@Mock
	private Logger logger;
	
	private CreateAccountConversationController spy;
	
	@Before
	public void before(){
		spy = Mockito.spy(CreateAccountConversationController.class);
		spy.setConversation(conversation);
		spy.setGoogleMapController(googleMapController);
		spy.setUserRepository(userRepository);
		spy.setLogger(logger);
	}
	
	@Test
	public void onDefaultSettingStage(){
		String result = spy.onDefaultSettingStage();
		assertTrue(spy.isPassPersonalData());
		assertEquals(result, spy.REDIRECT_TO_DEFAULT_SETTING);
	}
	
	@Test
	public void onFinalizeStage(){
		String result = spy.onFinalizeStage();
		assertTrue(spy.isPassDefaultSetting());
		assertEquals(result, spy.REDIRECT_TO_FINALIZE);
	}
	
	@Test
	public void onCreateAccountShouldSuccess(){
		User user = spy.getUser();
		Mockito.when(userRepository.create(Mockito.any())).thenReturn(user);
		String result = spy.onCreateAccount();
		assertEquals(result, spy.REDIRECT_TO_SUCCESS);
	}
	

	@Test
	public void onCreateAccountShouldNotSuccess(){
		Mockito.when(userRepository.create(Mockito.any())).thenThrow(new PersistenceException());
		String result = spy.onCreateAccount();
		assertEquals(result, spy.REDIRECT_TO_NOT_SUCCESS);
	}
	
	@Test
	public void redirectIfStageIsNotPassOrConversationIsTransientShouldRedirectToPersonalDate() throws IOException{
		FacesContext facesContext = ContextMocker.mockFacesContext();

		//mock
		ExternalContext externalContext = Mockito.mock(ExternalContext.class);
		
		//when
		Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
		Mockito.when(conversation.isTransient()).thenReturn(false);
		spy.setPassPersonalData(false);
		
		//execute
		spy.redirectIfStageIsNotPassOrConversationIsTransient("DEFAULT_SETTINGS");
		
		//veryfi
		Mockito.verify(facesContext, Mockito.times(1)).getExternalContext();
		Mockito.verify(externalContext, Mockito.times(1)).redirect(spy.PERSONAL_DATA_PAGE);
	}
	
	@Test
	public void redirectIfStageIsNotPassOrConversationIsTransientShouldNotRedirect() throws IOException{
		FacesContext facesContext = ContextMocker.mockFacesContext();

		//mock
		ExternalContext externalContext = Mockito.mock(ExternalContext.class);
		
		//when
		Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
		Mockito.when(conversation.isTransient()).thenReturn(false);
		spy.setPassPersonalData(true);
		
		//execute
		spy.redirectIfStageIsNotPassOrConversationIsTransient("DEFAULT_SETTINGS");
		
		//veryfi
		Mockito.verify(facesContext, Mockito.never()).getExternalContext();
	}
	
	@Test
	public void beginConversationShouldBegin(){
		FacesContext facesContext = ContextMocker.mockFacesContext();
		
		//when
		Mockito.when(facesContext.isPostback()).thenReturn(false);
		Mockito.when(conversation.isTransient()).thenReturn(true);
		
		//execute
		spy.beginConversation();
		
		//veryfi
		Mockito.verify(conversation, Mockito.times(1)).begin();
		
	}
	
	@Test
	public void beginConversationShouldNotBegin(){
		FacesContext facesContext = ContextMocker.mockFacesContext();
		
		//when
		Mockito.when(facesContext.isPostback()).thenReturn(false);
		Mockito.when(conversation.isTransient()).thenReturn(false);
		
		//execute
		spy.beginConversation();
		
		//veryfi
		Mockito.verify(conversation, Mockito.never()).begin();
		
	}
}
