package com.pw.lokalizator.security.restful;

import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.repository.UserRepository;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 * Created by wereckip on 18.08.2016.
 */

@Stateless
@DatabaseAuthenticat
public class DatabaseAuthenticateService implements AuthenticateService {
    @Inject
    private UserRepository userRepository;

    @Override
    public User authenticate(String login, String password) throws AuthenticateExcpetion {
        try{
            User user = userRepository.findUserFeatchRolesByLoginAndPassword(login, password);
            return user;
        } catch(EJBTransactionRolledbackException e){
            if(e.getCause() instanceof NoResultException)
                throw new AuthenticateExcpetion("Invalid login or password");

            throw new RuntimeException(e.getCause());
        }
    }
}
