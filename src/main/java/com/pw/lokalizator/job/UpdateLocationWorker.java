package com.pw.lokalizator.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.lokalizator.exception.GeocodeResponseException;
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
	
	@Schedule(minute="*/1", hour="*")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void work(){
		log.info("UpdateLocationWorker has started");
		try{
			
			Set<Location>locations = new LinkedHashSet<Location>( locationRepository.findWhereCityIsNull() );
			
			if(locations.size() > 0){
				
				log.info("All unique records " + locations.size());
				
				Queue<Callable<Location>>tasks = new LinkedList<Callable<Location>>();
				for(Location location : locations){
					tasks.add(new GeocodeCallable(location));
				}
				
				ScheduledExecutorService scheduledExecutorService = null;
				List<Future<Location>>futures = new ArrayList<Future<Location>>();
				Queue<Callable<Location>>tasksToExecute = new LinkedList<Callable<Location>>();
				
				log.info("Executing geocodecallable all tasks has began");
				long executingTasktime = System.currentTimeMillis();
				
				while(!tasks.isEmpty()){
					for(int i = 0; i<5 && !tasks.isEmpty(); i++){
						 tasksToExecute.add(tasks.poll());
					}
					
					scheduledExecutorService = Executors.newScheduledThreadPool( locations.size() < 5 ? locations.size() : 5);
					
					log.info("Executing geocodecallable " + tasksToExecute.size() + " tasks has began");
					long executingPackTaskTime = System.currentTimeMillis();
					futures.addAll(scheduledExecutorService.invokeAll(tasksToExecute, 1, TimeUnit.MINUTES));
					log.info("Executing geocodecallable " + tasksToExecute.size() + " tasks has ended after " + (System.currentTimeMillis() - executingPackTaskTime) + "ms");
					
					scheduledExecutorService.shutdown();
					
					//jesli nie minela sekunda
					long timeToSleep = System.currentTimeMillis() - executingPackTaskTime;
					if(timeToSleep < 1000){
						Thread.sleep(1000 - timeToSleep);
					}
					tasksToExecute.clear();
				}

				log.info("Executing GeocodeCallable  has ended after " + (System.currentTimeMillis() - executingTasktime) + "ms");
				
				updateCitys(futures);
			} else {
				log.info("No unique records");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*
	 * TRansacional method to update locations
	 */
	
	private void updateCitys(List<Future<Location>>futures){
		
		Iterator<Future<Location>> it = futures.iterator();
		Future<Location> future = null;
		Location location = null;
		while(it.hasNext()){
			try{
				if((future = it.next()) != null){
					if((location = future.get()) != null)
					  locationRepository.updateCity(location.getLatitude(), location.getLongitude(), location.getAddress());
				}
				
			}catch(GeocodeResponseException gre){
				log.error(gre.getMessage());
			}catch(Exception e){
				e.printStackTrace();
			}

		}
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
          
          Response response = target
        		                    .request()
        		                    .header("Accept-Language", "pl,en-US;q=0.7,en;q=0.3")
        		                    .header("Accept-Charset", "UTF-8")
        		                    .get();
          String value = response.readEntity(String.class);
          
          //Parse result to AddressComponent
          ObjectMapper mapper = new ObjectMapper();
          Geocode geocode = mapper.readValue(value, Geocode.class);
          
          if(geocode.getStatus().equalsIgnoreCase("OK")){
        	  
        	  location.setAddress(geocode
        			               .getResults()
        			               .get(0)
        			               .getFormattedAddress());
        	  return location;
          } 
          
          throw new GeocodeResponseException("Blad geocode response :" + geocode.getStatus() + " , Url :" + target.getUri().toString());
	  }
	}
}
