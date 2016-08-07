package com.pw.lokalizator.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.pw.lokalizator.jsf.utilitis.JsfMessageBuilder;
import com.pw.lokalizator.model.LokalizatorSession;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

@ViewScoped
@Named(value="lokalizatorSecurityController")
public class LokalizatorSecurityController implements Serializable{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LokalizatorSession lokalizatorSession;
	
	private String login;
	private String password;
	Logger logger = Logger.getLogger(LokalizatorSession.class);
	
	public String login(){
		try{
			User user = userRepository.findUserFeatchDefaultSettingByLoginAndPassword(login,password);
			lokalizatorSession.setUser(user);
			return "/location.xhtml?faces-redirect=true"; 
		} catch (Exception e){
			JsfMessageBuilder.errorMessage("Invalid or unknown credentials");
			logger.info("[LokalizatorSecurityController] Nie udana proba logowania na konto " + login + e.getMessage());
			return null;
		}
	}
	
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		logger.debug("[LokalizatorSecurityController] logout successful for " + login);
		return "/login.xhtml?faces-redirect=true";
	}
	
	public String redirectIfArleadyLogged(){
		if( lokalizatorSession.isLogged() )
			return "/logout.xhtml?faces-redirect=true";
		return null;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


}
