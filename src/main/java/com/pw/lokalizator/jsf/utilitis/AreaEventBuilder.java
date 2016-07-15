package com.pw.lokalizator.jsf.utilitis;

import java.util.Date;

import com.pw.lokalizator.model.entity.Area;
import com.pw.lokalizator.model.entity.AreaEvent;
import com.pw.lokalizator.model.entity.AreaEventGPS;
import com.pw.lokalizator.model.entity.AreaEventNetwork;
import com.pw.lokalizator.model.entity.AreaMessageMail;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.AreaFollows;
import com.pw.lokalizator.model.enums.AreaMailMessageModes;

public class AreaEventBuilder {

	public AreaEvent create(Area area, Location location){
		AreaEvent areaEvent = getAreaEventBasedOnLocationType(location);
		areaEvent.setArea(area);
		areaEvent.setDate(new Date());
		areaEvent.setMailSend( shouldSendMailMessage( area.getAreaMessageMail() ) );
		areaEvent.setMessage( createAreaEventMessage(area, location) );
		return areaEvent;
	}
	
	public String createUrl(AreaEvent areaEvent){
		return   "http://localhost:8080/lokalizator/areaEvent/event.xhtml?"
			   + "id="
			   + areaEvent.getId()
			   + "&provider="
			   + areaEvent.getLocation().getProviderType();
	}

	private String createAreaEventMessage(Area area, Location location){
		StringBuilder builder = new StringBuilder();
		
		builder.append(area.getTarget().getLogin())
		       .append( getMessagePartFollowType(area) )
		       .append(" dnia ")
		       .append(location.getDate())
		       .append(" (lokalizacja za pomoca ")
		       .append(getMessagePartLocationProvider(location))
		       .append(" )");
		
		return builder.toString();
	}
	
	private String getMessagePartLocationProvider(Location location){
		String provider = location.getProviderType().toString();
		
		if(location instanceof LocationNetwork){
			LocationNetwork locationNetwork = (LocationNetwork)location;
			provider += " ";
			provider += locationNetwork.getLocalizationServices();
		}
		
		return provider;
	}
	
	private String getMessagePartFollowType(Area area){
		if(area.getAreaFollowType() == AreaFollows.INSIDE)
			return " opuscil obszar sledzenia ( "
			       + area.getName()
			       + " )";
		else
			return " wszedl do obszaru sledzenia ( "
		           + area.getName()
			       + " )";
	}
	
	private AreaEvent getAreaEventBasedOnLocationType(Location location){
		if(location instanceof LocationNetwork)
			return  new AreaEventNetwork((LocationNetwork)location);
		else if(location instanceof LocationGPS)
		    return new AreaEventGPS((LocationGPS)location);
		else
			throw new IllegalArgumentException
			("Nie poprawy arguemnt, ta lokacja nie jest oblusgiwana " + location.getClass());	
	}
	
	private boolean shouldSendMailMessage(AreaMessageMail areaMessageMail){
		if( areaMessageMail.isActive() ){
			if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageModes.ACCEPT && areaMessageMail.isAccept()){
				return true;
			} else if( areaMessageMail.getAreaMailMessageMode() == AreaMailMessageModes.EVER ){
				return true;
			}
		}
		
		return false;
	}
}
