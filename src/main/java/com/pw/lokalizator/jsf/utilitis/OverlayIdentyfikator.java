package com.pw.lokalizator.jsf.utilitis;

import java.util.regex.Pattern;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.Overlays;
import com.pw.lokalizator.model.Providers;

public class OverlayIdentyfikator {
	
	private Long id;
	private String login;
	private Providers providerType;
	private Overlays overlayType;
	private Pattern pattern;

	public OverlayIdentyfikator(Location location, Overlays overlayType){
		this.id = location.getId();
		this.login = location.getUser().getLogin();
		this.providerType = location.getProvider();
		this.overlayType = overlayType;
		
		pattern = createPattern();
	}
	
	public boolean matches(String id){
		return pattern.matcher(id).matches();
	}
	
	public OverlayIdentyfikator(Overlays overlayType, String login, Providers providerType, Long id){
		this.id = id;
		this.login = login;
		this.providerType = providerType;
		this.overlayType = overlayType;
		
		pattern = createPattern();
	}
	
	private OverlayIdentyfikator(long id, String login, Providers providerType, Overlays overlayType){
		this.id = id;
		this.login = login;
		this.providerType = providerType;
		this.overlayType = overlayType;
	}
	
	private Pattern createPattern(){
		StringBuilder regex = new StringBuilder();
		
		regex.append(addOverlayTypeSection());
		regex.append("_");
		regex.append(addLoginSection());
		regex.append("_");
		regex.append(addProviderTypeSection());
		regex.append("_");
		regex.append(addIdSection());
		
		return Pattern.compile(regex.toString());
	}
	
	public String createIdentyfikator(){
		//"type_login_priovider_id"
		String identyfikator = 
				    overlayType +
				    "_" +
				    login + 
				    "_" +
				    providerType + 
				    "_" + 
				    id;
		return identyfikator;
	}
	
	private String addIdSection(){
		if(id == null)
			return ".+";
		else
			return String.valueOf(id);
	}
	
	private String addOverlayTypeSection(){
		if(overlayType == null)
			return ".+";
		else
			return overlayType.toString();
	}
	
	private String addProviderTypeSection(){
		if(providerType == null)
			return ".+";
		else
			return providerType.toString();
	}
	
	private String addLoginSection(){
		if(login == null)
			return ".+";
		else
			return login;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Providers getProviderType() {
		return providerType;
	}

	public void setProviderType(Providers providerType) {
		this.providerType = providerType;
	}

	public Overlays getOverlayType() {
		return overlayType;
	}

	public void setOverlayType(Overlays overlayType) {
		this.overlayType = overlayType;
	}
	
	public static class OverlayIdentyfikatorBuilder{
		private Long id;
		private String login;
		private Providers providerType;
		private Overlays overlayType;
		
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
		
		public OverlayIdentyfikatorBuilder overlayType(Overlays overlayType){
			this.overlayType = overlayType;
			return this;
		}
		
		public OverlayIdentyfikator build(){
			return new OverlayIdentyfikator(
					id,
					login,
					providerType,
					overlayType
					);
		}
	}
}
