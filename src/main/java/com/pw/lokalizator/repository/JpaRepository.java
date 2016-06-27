package com.pw.lokalizator.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

@Local
public interface JpaRepository<T, ID extends Serializable> {
	T create(T entity);
	T save(T entity);
	void delete(T entity);
	void delete(ID id);
	T findById(ID id);
	List<T> findAll();
}
