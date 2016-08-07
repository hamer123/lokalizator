package com.pw.lokalizator.jsf.utilitis.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

@FacesValidator(value = "loginUsedValidator")
public class LoginUsedValidator implements Validator{

	@Inject
	private UserRepository userRepository;
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String login = (String)value;
		User user = null;
		
		try{
			user = userRepository.findByLogin(login);
		}catch(Exception e){
			//
		} finally{
			if(user != null){
				FacesMessage msg = new FacesMessage("Login: Login jest juz zajety");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

}
