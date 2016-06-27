package com.pw.lokalizator.service;

import javax.ejb.Local;

import com.pw.lokalizator.model.entity.AreaEvent;

@Local
public interface AreaMessageService {
	void sendMessage(AreaEvent areaEvent);
}
