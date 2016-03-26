package com.pw.lokalizator.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.jboss.logging.Logger;
import com.pw.lokalizator.model.User;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="lokalizatorSession")
@SessionScoped
public class LokalizatorSession implements Serializable{
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(LokalizatorSession.class);
	@EJB
	private UserRepository userRepository;
	
	private boolean isLogged;
	private String userLogin;
	private String userPassword;
	private User currentUser;
	
	public String login(){
		try{
			currentUser = userRepository.findByLoginAndPassword(userLogin,userPassword);
		}catch(Exception e){
			e.printStackTrace();
			//TODO
		}
		
		if(currentUser != null){
			isLogged = true;
			userPassword = "";
			
			log.info("login successful for " + userLogin);
			return "/location.xhtml?faces-redirect=true";
		}else{
			log.info("failed to login for " + userLogin);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Login failed",
							"Invalid or unknown credentials."));
			
			return null;
		}
	}
	
	
	public void checkIsLogged() throws IOException{
		if(isLogged)
			FacesContext.getCurrentInstance().getExternalContext().redirect("logout.xhtml");
	}
	
	public String logout(){
		log.debug("invalidating session for " + userLogin);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		log.debug("logout successful for " + userLogin);
		
		return "/login.xhtml?faces-redirect=true";
	}


	public String getUserLogin() {
		return userLogin;
	}


	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}


	public String getUserPassword() {
		return userPassword;
	}


	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}


	public boolean isLogged() {
		return isLogged;
	}


	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}


	public User getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
}
