package com.pw.lokalizator.repository;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.AreaMessageMail;

@Local
public interface AreaMessageMailRepository extends JpaRepository<AreaMessageMail, Long> {

	int updateAcceptById(long id, boolean accept);
}
