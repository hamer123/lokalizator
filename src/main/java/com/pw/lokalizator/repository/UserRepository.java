package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;
@Local
public interface UserRepository extends JpaRepository<User, Long> {
	User findByLoginAndPassword(String login, String password);
	User findByLogin(String login);
    List<User> findByLoginLike(String loginLike);
    
    User findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(Long id);
}
