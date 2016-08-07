package com.pw.lokalizator.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaPointRepository;
import com.pw.lokalizator.service.GoogleMapUserComponentService;

@RunWith(MockitoJUnitRunner.class)
public class AreaEventControllerTest {
	@Mock
	private AreaEventGPSRepository areaEventGPSRepository;
	@Mock
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Mock
	private AreaPointRepository areaPointRepository;
	@Mock
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Mock
	private UserGoogleMapController googleMapController;
	
	private AreaEventController spy;
	
	@Before
	public void before(){
		spy = Mockito.spy(AreaEventController.class);
		
		spy.setAreaEventGPSRepository(areaEventGPSRepository);
		spy.setAreaEventNetworkRepository(areaEventNetworkRepository);
		spy.setGoogleMapUserComponentService(googleMapUserComponentService);
		spy.setAreaPointRepository(areaPointRepository);
		spy.setGoogleMapController(googleMapController);
	}
	
	@Test
	public void test(){
		
	}
	
}
