package com.pw.lokazaliator.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;

public class OverlayIdentyfikatorTest {
	
	private OverlayIdentyfikator identyfikator;
	
	@Before
	public void before(){
		identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
				                                         .providerType(Providers.GPS)
		                                                 .login("hamer123")
		                                                 .id(123L)
		                                                 .build();
	}
	
	@Test
	public void testRegex(){
		String regex = "MARKER_GPS_.+_hamer123_123";
	    assertEquals(regex, identyfikator.createPattern().toString());
	}
	
	@Test
	public void testIdentyfikator(){
		String regex = "MARKER_GPS_null_hamer123_123";
	    assertEquals(regex, identyfikator.createIdentyfikator());
	}
	
	@Test
	public void testFields(){
		assertEquals("hamer123", identyfikator.getLogin());
		assertEquals(Providers.GPS, identyfikator.getProviderType());
		assertEquals(123L, identyfikator.getId());
		assertEquals(Overlays.MARKER, identyfikator.getOverlay());
		assertEquals(null, identyfikator.getLocalizationServices());
	}
	
}
