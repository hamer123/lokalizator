package com.pw.lokalizator.singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.pw.lokalizator.jsf.utilitis.PropertiesReader;

@Named
@Singleton
public class LokalizatorProperties {
	private final Map<String,Object>properties = new HashMap<>();
	
	public static final String GOOGLEMAP_DEFAULT_ZOOM = "GOOGLEMAP_DEFAULT_ZOOM";
	public static final String GOOGLEMAP_DEFAULT_CENTER = "GOOGLEMAP_DEFAULT_CENTER";
	
	@PostConstruct
	public void postConstruct(){
		PropertiesReader reader = new PropertiesReader("lokalizator");
		Object result = null;
		
		result = reader.findPropertyByName("GOOGLEMAP_DEFAULT_ZOOM");
		properties.put(GOOGLEMAP_DEFAULT_ZOOM, Integer.valueOf((String)result));
		
		result = reader.findPropertyByName("GOOGLEMAP_DEFAULT_CENTER");
		properties.put(GOOGLEMAP_DEFAULT_CENTER, result);
	}
	
	public Object getPropertie(String name){
		return properties.get(name);
	}
}
