package com.pw.lokalizator.file.storage;

public class StorageNotCreated extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public StorageNotCreated(String msg){
		super(msg);
	}
}
