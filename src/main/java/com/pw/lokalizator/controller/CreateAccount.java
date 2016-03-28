package com.pw.lokalizator.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.pw.lokalizator.model.User;
import com.pw.lokalizator.service.UserService;

//@Named(value="account")
@RequestScoped
public class CreateAccount implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB
	UserService userService;
	
	private String login;
	private String password;
	private String email;
	
	public void createAccount(){
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		user.setEmail(email);
		
		try{
			userService.createAccount(user);
			
		}catch(Exception e){
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Create account failed",
							"Login lub email jest juz zajety"));
		}
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Create account",
						"Account has been created"));
		
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
}
