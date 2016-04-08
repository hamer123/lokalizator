package com.pw.lokalizator.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LocationUpdateService extends ScheduledThreadPoolExecutor{

	public LocationUpdateService(int corePoolSize) {
		super(corePoolSize);
	}
	

}
