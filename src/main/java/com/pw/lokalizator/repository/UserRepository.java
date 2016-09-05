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
	User findUserFeatchRolesByLoginAndPassword(String login, String password);
	User findByLogins(String login);
	List<User> findByLogins(List<String> logins);
	User findByLoginFetchArea(String login);
    List<String> findLoginByLoginLike(String loginLike);
    List<User> findByIds(Set<Long>id);
    User findByEmail(String email);
}
