package com.pw.lokalizator.model;

public class NotSingleResultException extends RuntimeException {
	public NotSingleResultException(){
		super("Znaleziono wiecej niz 1 pasujacy rekord");
	}
}
