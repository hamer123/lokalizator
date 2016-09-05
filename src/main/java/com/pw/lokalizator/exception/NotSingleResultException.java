package com.pw.lokalizator.exception;

public class NotSingleResultException extends RuntimeException {
	public NotSingleResultException(){
		super("Znaleziono wiecej niz 1 pasujacy rekord");
	}
}
