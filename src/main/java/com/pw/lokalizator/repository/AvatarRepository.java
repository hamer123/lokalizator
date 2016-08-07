package com.pw.lokalizator.repository;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.Avatar;

@Local
public interface AvatarRepository extends JpaRepository<Avatar, Long>{

}
