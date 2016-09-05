package com.pw.lokalizator.model.session;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.enums.Roles;

@Named
@SessionScoped
public class LokalizatorSession implements Serializable{
	private User user;

	public boolean isInRole(Roles role){
		for(Roles _role : user.getRoles())
			if(_role.equals(role))
				return true;
		return false;
	}

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
