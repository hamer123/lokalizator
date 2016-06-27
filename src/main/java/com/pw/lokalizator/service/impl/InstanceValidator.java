package com.pw.lokalizator.service.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.pw.lokalizator.model.entity.CellInfoGSM;
import com.pw.lokalizator.model.entity.CellInfoLte;

@Named
@ApplicationScoped
public class InstanceValidator {

	public boolean isInstaceOf(Object obj, String name){
		try {
			return Class.forName(name).isInstance(obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
