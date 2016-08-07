package com.pw.lokalizator.controller;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.pw.lokalizator.model.LokalizatorSession;

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
