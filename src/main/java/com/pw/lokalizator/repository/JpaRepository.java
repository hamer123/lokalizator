package com.pw.lokalizator.repository;

import java.io.Serializable;
import java.util.Collection;
import javax.ejb.Local;

@Local
public interface JpaRepository<T, ID extends Serializable> {
	T create(T entity);
	T save(T entity);
	void remove(T entity);
	void remove(ID id);
	T findById(ID id);
	Collection<T> findAll();
}
