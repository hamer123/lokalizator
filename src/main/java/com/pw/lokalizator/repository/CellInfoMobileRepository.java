package com.pw.lokalizator.repository;

import com.pw.lokalizator.model.entity.CellInfoMobile;

public interface CellInfoMobileRepository extends JpaRepository<CellInfoMobile, Long>{

	CellInfoMobile findByLocationId(Long id);
}
