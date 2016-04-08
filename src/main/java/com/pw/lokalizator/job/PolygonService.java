package com.pw.lokalizator.job;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PolygonService {

	@Schedule(minute="*", hour="*/12")
	public void checkIsUserInPolygon(){
		
	}
}
