package com.pw.lokalizator.jsf.utilitis;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class JsfMessageBuilder {
	
	private static final String MESSAGE_PROPERTIES_NAME = "message.properties";
	
	public static void errorMessageBundle(String bundle){
		PropertiesReader propertiesReader = new PropertiesReader(MESSAGE_PROPERTIES_NAME);
		String msg = propertiesReader.findPropertyByName(bundle);
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void infoMessageBundle(String bundle){
		PropertiesReader propertiesReader = new PropertiesReader(MESSAGE_PROPERTIES_NAME);
		String msg = propertiesReader.findPropertyByName(bundle);
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void warnMessageBundle(String bundle){
		PropertiesReader propertiesReader = new PropertiesReader(MESSAGE_PROPERTIES_NAME);
		String msg = propertiesReader.findPropertyByName(bundle);
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void errorMessage(String msg){		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void infoMessage(String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void warnMessage(String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
