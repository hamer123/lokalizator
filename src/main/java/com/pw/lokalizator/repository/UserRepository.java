package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.User;
@Local
public interface UserRepository extends JpaRepository<User, Long> {
	User findUserWithSecurityByLoginAndPassword(String login, String password);
	User findByLogin(String login);
    List<String> findLoginByLoginLike(String loginLike);
    
    User findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(Long id);
    User findUserWithPolygonsByLogin(String login);
}
