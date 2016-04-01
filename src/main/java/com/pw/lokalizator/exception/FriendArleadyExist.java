package com.pw.lokalizator.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class FriendArleadyExist extends RuntimeException{
	public FriendArleadyExist(String e){
		super(e);
	}
}
