package com.pw.lokalizator.repository;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface Dao<T> {

	void persist(T e);
	T merge(T e);
	T findById(long id);
	List<T> findAll();
	//void remove(T e);
}
