package com.pw.lokalizator.jsf.utilitis;

import java.util.regex.Pattern;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;

public class OverlayIdentyfikator {
	private long id;
	private String login;
	private Providers providerType;
	private LocalizationServices localizationServices;
	private Overlays overlay;

	public OverlayIdentyfikator(Location location){
		this.id = location.getId();
		this.login = location.getUser().getLogin();
		this.providerType = location.getProviderType();
		if(location.getProviderType() == Providers.NETWORK)
			this.localizationServices = getLocalizationServicesFromLocation(location);
	}
	
	public OverlayIdentyfikator(Location location, Overlays overlay){
		this.id = location.getId();
		this.login = location.getUser().getLogin();
		this.providerType = location.getProviderType();
		this.overlay = overlay;
		if(location.getProviderType() == Providers.NETWORK)
			this.localizationServices = getLocalizationServicesFromLocation(location);
	}
	
	private OverlayIdentyfikator(Overlays overlay, Providers providerType, LocalizationServices localizationServices, String login, long id){
		this.overlay = overlay;
		this.id = id;
		this.login = login;
		this.providerType = providerType;
		this.localizationServices = localizationServices;
	}

	private LocalizationServices getLocalizationServicesFromLocation(Location location){
		LocationNetwork locationNetwork = (LocationNetwork)location;
		return locationNetwork.getLocalizationServices();
	}
	
	public Pattern createPattern(){
		StringBuilder regex = new StringBuilder();
		
		regex.append(getOverlaySection())
		     .append("_")
		     .append(getProviderTypeSection())
		     .append("_")
		     .append(getLocalizationServicesSection())
		     .append("_")
		     .append(getLoginSection())
		     .append("_")
		     .append(getIdSection());
		
//		System.out.println(regex.toString());
		
		return Pattern.compile(regex.toString());
	}
	
	public String createIdentyfikator(){
		StringBuilder identyfikator = new StringBuilder();
		
		identyfikator.append(overlay)
		  .append("_")
		  .append(providerType)
		  .append("_")
		  .append(localizationServices)
		  .append("_")
		  .append(login)
		  .append("_")
		  .append(id);
		
//		System.out.println(identyfikator.toString());
		
		return identyfikator.toString();
	}
	
	private String getIdSection(){
		if(id == 0)
			return ".+";
		else
			return String.valueOf(id);
	}
	
	private String getOverlaySection(){
		if(overlay == null)
			return ".+";
		
		return overlay.toString();
	}
	
	private String getLocalizationServicesSection(){
		if(localizationServices == null)
			return ".+";
		else
			return localizationServices.toString();
	}
	
	private String getProviderTypeSection(){
		if(providerType == null)
			return ".+";
		else
			return providerType.toString();
	}
	
	private String getLoginSection(){
		if(login == null)
			return ".+";
		else
			return login;
	}

	public long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}


	public Providers getProviderType() {
		return providerType;
	}

	public LocalizationServices getLocalizationServices() {
		return localizationServices;
	}

	public Overlays getOverlay() {
		return overlay;
	}



	public static class OverlayIdentyfikatorBuilder{
		private long id;
		private String login;
		private LocalizationServices localizationServices;
		private Providers providerType;
		private Overlays overlay;
		
		public OverlayIdentyfikatorBuilder id(long id){
			this.id = id;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder login(String login){
			this.login = login;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder providerType(Providers providerType){
			this.providerType = providerType;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder localzationServiceType(LocalizationServices localizationServices){
			this.localizationServices = localizationServices;
			return this;
		}
		
		public OverlayIdentyfikatorBuilder overlayType(Overlays overlay){
			this.overlay = overlay;
			return this;
		}
		
		public OverlayIdentyfikator build(){
			return new OverlayIdentyfikator(
					overlay,
					providerType,
					localizationServices,
					login,
					id
					);
		}
	}
}
