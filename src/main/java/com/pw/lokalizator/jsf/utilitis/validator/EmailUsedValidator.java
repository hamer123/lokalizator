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

@FacesValidator("emailUsedValidator")
public class EmailUsedValidator implements Validator{

	@Inject
	private UserRepository userRepository;
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		User user = null;
		try{
			String email = (String)value;
			user = userRepository.findByEmail(email);
		} catch(Exception e){
			//
		} finally{
			if(user != null){
				FacesMessage msg = new FacesMessage("Email: Email jest juz zajety");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

}
