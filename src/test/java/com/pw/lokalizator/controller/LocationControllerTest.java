package com.pw.lokalizator.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import com.pw.lokalizator.model.session.LokalizatorSession;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.AreaEventGPSRepository;
import com.pw.lokalizator.repository.AreaEventNetworkRepository;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.repository.CellInfoMobileRepository;
import com.pw.lokalizator.repository.UserRepository;
import com.pw.lokalizator.repository.WifiInfoRepository;
import com.pw.lokalizator.serivce.qualifier.DialogUserLocationGoogleMap;
import com.pw.lokalizator.serivce.qualifier.UserGoogleMap;
import com.pw.lokalizator.service.GoogleMapUserComponentService;
import com.pw.lokalizator.singleton.RestSessionManager;

@RunWith(MockitoJUnitRunner.class)
public class LocationControllerTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private WifiInfoRepository wifiInfoRepository;
	@Mock
	private CellInfoMobileRepository cellInfoMobileRepository;
	@Mock
	private AreaRepository areaRepository;
	@Mock
	private AreaEventNetworkRepository areaEventNetworkRepository;
	@Mock
	private AreaEventGPSRepository areaEventGPSRepository;
	@Mock @UserGoogleMap
	private GoogleMapController googleMapController;
	@Mock @DialogUserLocationGoogleMap
	private DialogUserLocationGoogleMapController googleMapSingleUserDialogController;
	@Mock
	private GoogleMapUserComponentService googleMapUserComponentService;
	@Mock
	private RestSessionManager restSessionManager;
	@Mock
	private LokalizatorSession lokalizatorSession;
	@Mock
	private Logger logger;
	
	private LocationController spy;
	
	@Before
	public void before(){
		spy = Mockito.spy(new LocationController());
		spy.setRestSessionManager(restSessionManager);
		spy.setLokalizatorSession(lokalizatorSession);
		spy.setGoogleMapController(googleMapController);
		spy.setAreaEventGPSRepository(areaEventGPSRepository);
		spy.setAreaEventNetworkRepository(areaEventNetworkRepository);

		//init user set
		Map<String,User>users = new HashMap<>();
		
		User user = new User();
		user.setLogin("hamer123");
		users.put(user.getLogin(), user);
		
		user = new User();
		user.setLogin("hamer1234");
		users.put(user.getLogin(), user);
		
		spy.setUsers(users);
	}
	
	@Test
	public void isUserArleadyOnListShouldReturnTrue() throws Exception{
		//test
		assertNotNull(spy.getUsers());
		assertEquals(true, spy.isUserArleadyOnList("hamer123"));
	}
	
	@Test
	public void isUserArleadyOnListShouldReturnFalse() throws Exception{
		//test
		assertNotNull(spy.getUsers());
		assertEquals(false, spy.isUserArleadyOnList("hamer12345"));
	}
	
	@Test
	public void filterLoginsShouldReturnListWithOneRecord(){
		//init
		List<String>logins = new ArrayList<>();
		logins.add("hamer123");
		logins.add("hamer12345");
		//execute
		List<String>results = spy.filterLogins(logins);
		//test
		assertEquals(1, results.size());
		assertEquals(results.get(0), "hamer12345");
	}
	
	@Test
	public void removeUserFromListShouldRemoveRecord(){
		//init
		Map<String,User>users = new HashMap<>();
		User user = new User();
		user.setLogin("qwerty");
		users.put(user.getLogin(), user);
		//exectue
		spy.removeUserFromFollow(user);
		//verify
		Mockito.verify(spy, Mockito.times(1)).removeUserFromFollow(user);
		assertNull(spy.getSelectUser());
		assertEquals(false, spy.getUsers().values().contains(user));
	}
	
	@Test
	public void refreshLastLocationsShouldChangeReferencesStats(){
		//init
		User arg = new User();
		arg.setId(1L);
		
		User user = new User();
		user.setId(1L);
		LocationGPS gps = new LocationGPS();
		LocationNetwork netNasz = new LocationNetwork();
		LocationNetwork netObcy = new LocationNetwork();
		user.setLastLocationGPS(gps);
		user.setLastLocationNetworkNaszaUsluga(netNasz);
		user.setLastLocationNetworObcaUsluga(netObcy);
		
		//mock
		UserRepository mock = Mockito.mock(UserRepository.class);
		spy.setUserRepository(mock);
		
		//when
		Mockito.when(mock.findById(1L)).thenReturn(user);
		
		//execute
		spy.refreshLastLocations(arg);
		
		//verify
		Mockito.verify(spy, Mockito.times(1)).refreshLastLocations(arg);
		assertEquals(gps, arg.getLastLocationGPS());
		assertEquals(netNasz, arg.getLastLocationNetworkNaszaUsluga());
		assertEquals(netObcy, arg.getLastLocationNetworObcaUsluga());
	}
	
	
	@Test
	public void createPolygonsShouldReturnEqualsList(){
		//skip
	}
	
//	@Test
	public void checkAreaEventShouldReturnListWithOneRecord(){
		//init
		long time = System.currentTimeMillis() - spy.ONE_MINUTE;
		Date date = new Date(time);
		
		Set<Long>ids = new HashSet<>();
		ids.add(1L);
		spy.setActiveAreaIds(ids);
		
		AreaEventGPS event = new AreaEventGPS();
		List<AreaEventGPS>events = new ArrayList<>();
		events.add(event);
		
		//when
		Mockito.when(areaEventGPSRepository.findByAreaIdAndDate(Mockito.eq(1L), Mockito.any())).thenReturn(events);
		Mockito.when(areaEventGPSRepository.findByAreaIdAndDate(Mockito.anyLong(), Mockito.any())).thenReturn(new ArrayList<AreaEventGPS>());
		
		//execute
		List<AreaEvent>result = spy.checkAreaEvent();
		
		//verify
		assertEquals(1, result.size());
		assertTrue(result.get(0).equals(event));
		assertNotNull(areaEventGPSRepository);
		
	}
	
	@Test
	public void removeUserComponentsFromGoogleMapShouldRemoveComponents(){
	}
	
	
}
