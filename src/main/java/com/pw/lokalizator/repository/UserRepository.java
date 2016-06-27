package com.pw.lokalizator.repository;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.entity.LocationGPS;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.User;
@Local
public interface UserRepository extends JpaRepository<User, Long> {
	User findUserWithSecurityByLoginAndPassword(String login, String password);
	User findByLogin(String login);
	List<User> findByLogin(List<String> logins);
	
	User findByLoginFetchArea(String login);
	
	
    List<String> findLoginByLoginLike(String loginLike);
    User findByIdGetIdAndLoginAndCurrentLocationsForAllProviders(Long id);
    User findUserWithPolygonsByLogin(String login);
    List<User> findById(Set<Long>id);
    int updateCurrentLocationNetworkNaszaUsluga(long locationId, long userId);
    int updateCurrentLocationNetworkObcaUsluga(long locationId, long userId);
    int updateCurrentLocationGPS(long locationId, long userId);
    
    
    
    List<User> findByIdFetchEagerLastLocations(Set<Long>id);
    User findByIdFetchEagerLastLocations(String login);
}
