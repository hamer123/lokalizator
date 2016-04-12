package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.Location;
import com.pw.lokalizator.model.User;

@Local
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByLoginAndPassword(String login, String password);
	public User findByLogin(String login);
	public List<User> findFriendsById(long id);
	public int persistFriend(long reciverId, long senderId);
}
