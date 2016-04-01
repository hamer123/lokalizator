package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.Friend;

@Local
public interface FriendRepository extends JpaRepository<Friend, Long>{
	public List<Friend>findByUserId(Long id);
}
