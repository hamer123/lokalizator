package com.pw.lokazaliator.test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.pw.lokalizator.controller.GoogleMapFollowUsersController;
import com.pw.lokalizator.jsf.utilitis.MarkerBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.entity.CellInfoGSM;
import com.pw.lokalizator.model.entity.CellInfoMobile;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.repository.AreaRepository;
import com.pw.lokalizator.repository.UserRepository;

@Named
@ViewScoped
@Transactional
public class TestCdiTransactional implements Serializable{

	@PersistenceContext
	EntityManager em;
	
	@Inject
	GoogleMapFollowUsersController controller;
	
	@EJB
	UserRepository repository;
	
	
	@PostConstruct
	private void init(){}
	
	@Transactional
	public void doIt(){

	}
	
	@Transactional
	public void onremove(){
		
	}
	
	@Transactional
	public void doTestTransaction(){

		doIt();
		
		
//		Area area = em.find(Area.class, 1L);
//		em.remove(area);
		
//		list.get(0).getPoints().containsKey(null);
		
		
//		String query2 = "SELECT new com.pw.lokalizator.model.entity.PolygonModel(p.id, p.name, p.polygonFollowType, t.id, t.login) FROM PolygonModel p INNER JOIN p.target t WHERE p.provider.id =:id";
//		
//		List<PolygonModel>list = em.createQuery(query2, PolygonModel.class)
//		  .setParameter("id", 1L)
//		  .getResultList();
//		
//		for(PolygonModel polygon : list){
//			System.out.println(polygon.getId() + ", " + polygon.getName() + ", " + polygon.getPolygonFollowType() + ", " + polygon.getTarget().getLogin());
//		}
//		
//		String query = "SELECT p FROM PolygonModel p WHERE p.provider.id =:id";
//		List<PolygonModel>list = em.createQuery(query, PolygonModel.class)
//		   .setParameter("id", 1L)
//		   .getResultList();
//		
//		PolygonModel polygon = list.get(0);
//		
//		System.out.println(polygon.getTarget().getLogin());
		  
		
		
//		CellInfoMobile cellInfoMobile = new CellInfoMobile();
//		CellInfoGSM gsm = new CellInfoGSM();
//		gsm.setCid(1);
//		
//		em.persist(cellInfoMobile);
//		em.persist(gsm);
//		
//		CellInfoMobile cellInfoMobile2 = em.find(CellInfoMobile.class, 2L);
//		System.out.println(cellInfoMobile2.getClass());
	}
	
//	public void findUserOnline(){
//		
//		MapModel model = controller.getGoogleMapModel();
//		model.addOverlay(new Marker(new LatLng(51.6014053, 18.9724216)));
//		System.out.println(model.getCircles().size());
//		
//		
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, -1);
//		
//		String query = "SELECT u.id, u.login, l.latitude, l.longitude, l.provider, l.address, l.date, l.user_id "
//				     + "FROM user as u "
//				     + "INNER JOIN location as l "
//				     + "ON u.lastGpsLocation_id = l.id "
//			         + "WHERE l.date > :date";
//		String query2 = "SELECT u.id, u.login, l.latitude, l.longitude, l.provider, l.address, l.date "
//				      + "FROM user as u INNER JOIN location as l "
//				      + "ON u.id = l.user_id "
//			          + "WHERE l.date > '2016-04-30 05:21:24' AND u.id = :id";
//		String query3 = "SELECT l FROM Location l WHERE l.id IN (:idw)";
//		
//		String query4 = "SELECT u.id, u.login, l.id, l.address, l.date, l.latitude, l.longitude, l.provider "
//				      + "FROM location as l "
//				      + "INNER JOIN user as u "
//				      + "ON l.user_id = :id ";
//		
//		String query5 = "SELECT new com.pw.lokalizator.model.User(u.id, u.login) FROM User u WHERE u.login LIKE :login";
//		
//		String query6 = "SELECT l.id, l.address, l.date, l.latitude, l.longitude, l.provider, l.user_id, u.login "
//				      + "FROM location as l "
//				      + "INNER JOIN user as u "
//				      + "ON l.user_id = u.id "
//				      + "WHERE l.id = (SELECT lastGpsLocation_id FROM user WHERE login = :login) OR l.id = (SELECT lastNetworkLocation_id FROM user WHERE login = :login) OR l.id = (SELECT lastOwnProviderLocation_id FROM user WHERE login = :login)";
//		
//	}
//
//	public GoogleMapController getController() {
//		return controller;
//	}
//
//	public void setController(GoogleMapController controller) {
//		this.controller = controller;
//	}
//	
//    private MapModel simpleModel;
//    
//    @PostConstruct
//    public void init() {
//    	
//    	System.out.println("???");
//    	
//        simpleModel = new DefaultMapModel();
//          
////        //Shared coordinates
//        LatLng coord1 = new LatLng(36.879466, 30.667648);
////        LatLng coord2 = new LatLng(36.883707, 30.689216);
////        LatLng coord3 = new LatLng(36.879703, 30.706707);
////        LatLng coord4 = new LatLng(36.885233, 30.702323);
////          
////        //Basic marker
//        simpleModel.addOverlay(new Marker(coord1, "Konyaalti"));
////        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));
////        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));
////        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));
//        
//		User user = new User();
//		user.setId(1L);
//		user.setLogin("hamer123");
//		
//        Location location = new Location();
//        location.setId(1);
//        location.setDate(new Date());
//        location.setLatitude(51.6014053);
//        location.setLongitude(18.9724216);
//        location.setProvider(Providers.GPS);
//        location.setUser(user);
//
//        
//        simpleModel.addOverlay(MarkerBuilder.createMarker(location));
//        
//		controller.addUser(user.getLogin());
//		controller.update();
//    }
//  
//    public MapModel getSimpleModel() {
//        return simpleModel;
//    }
}
