package com.pw.lokalizator.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.lokalizator.google.AddressComponent;
import com.pw.lokalizator.google.Geocode;
import com.pw.lokalizator.google.Result;
import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.repository.LocationRepository;

@Stateless
public class UpdateLocationWorker {
	@EJB
	private LocationRepository locationRepository;
	
	Logger log = Logger.getLogger(UpdateLocationWorker.class);
	
	private String GOOGLE_MAP_GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json";
	
	@Schedule(second="*/10", minute="*", hour="*")
	public void work(){
		log.info("UpdateLocationWorker has started");
		long time = System.currentTimeMillis();
		
		try{
			
			Set<Location>locations = new HashSet<Location>( locationRepository.findWhereCityIsNull() );
			
			log.info("All unique records lat lon " + locations.size());
			
			List<Callable<Location>>tasks = new ArrayList<Callable<Location>>();
			
			for(Location location : locations)
				tasks.add(new GeocodeCallable(location));
			

			//Max 25 threads
			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool( locations.size() < 25 ? locations.size() : 25 );
			
			log.info("Executing GeocodeCallable tasks has began");
			
			long executingTasktime = System.currentTimeMillis();
			List<Future<Location>>futures = scheduledExecutorService.invokeAll(tasks, 1, TimeUnit.MINUTES);
			
			log.info("Executing GeocodeCallable  has ended after " + (System.currentTimeMillis() - executingTasktime) + "ms");
			
			Iterator<Future<Location>> it = futures.iterator();
			while(it.hasNext()){
				try{
					Future<Location> future = it.next();
					Location location = future.get();
					locationRepository.save(location);
					
				}catch(Exception e){
					//TODO 
					e.printStackTrace();
				}

			}
			
		}catch(Exception e){
			//TODO
			e.printStackTrace();
		}
		
		log.info("UpdateLocationWorker finished after " + (System.currentTimeMillis() - time) + "ms");
	}
	
	/*
	 * Callable to handle request to google map and parse result to object
	 */
	public class GeocodeCallable implements Callable<Location>{
	
	  private Location location;
	  
	  public GeocodeCallable(Location location){
		  this.location = location;
	  }
		
	  @Override
	  public Location call() throws Exception {
		  //Create parameter to URL query
		  String param = location.getLatitude() + ", " + location.getLongitude();
		  
		  //Send request at endpoint to get geolocation
          Client client = ClientBuilder.newBuilder().build();
          WebTarget target = client.target(GOOGLE_MAP_GEO_URL + "?&latlng=" + param);
          Response response = target.request().get();
          String value = response.readEntity(String.class);
          
          //Parse result to AddressComponent
          ObjectMapper mapper = new ObjectMapper();
          Geocode geocode = mapper.readValue(value, Geocode.class);
          
          if(geocode.getStatus().equalsIgnoreCase("OK")){
        	  
        	  for(Result result : geocode.getResults()){
        		  System.out.println(result.getFormattedAddress());
        	  }
        	  
        	  location.setAddress(geocode
        			               .getResults()
        			               .get(0)
        			               .getFormattedAddress());
        	  return location;
          } else {
              throw new RuntimeException("Blad response z geocode " + geocode.getStatus());
          }
          
	  }
	}
}
