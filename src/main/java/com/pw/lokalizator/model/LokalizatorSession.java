package com.pw.lokalizator.model;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

@Named(value="lokalizatorSession")
@SessionScoped
public class LokalizatorSession implements Serializable{
	private User user;
	
	public boolean isLogged(){
		return user != null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
