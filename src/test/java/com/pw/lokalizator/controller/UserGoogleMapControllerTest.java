package com.pw.lokalizator.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.pw.lokalizator.model.session.LokalizatorSession;

@RunWith(MockitoJUnitRunner.class)
public class UserGoogleMapControllerTest {
	@Mock
	private LokalizatorSession lokalizatorSession;
	@Spy
	private UserGoogleMapController spy;
	
	@Before
	public void before(){
		spy.setLokalizatorSession(lokalizatorSession);
	}
	
	@Test
	public void addOverlayShouldAddList(){
		
	}
	
}
