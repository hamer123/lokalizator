package com.pw.lokalizator.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AreaMialMessageException extends RuntimeException{

	public AreaMialMessageException(Throwable throwable){
		super(throwable);
	}
}
