package com.pw.lokalizator.singleton;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.pw.lokalizator.file.storage.AvatarImageStorage;
import com.pw.lokalizator.file.storage.FileStorage;
import com.pw.lokalizator.file.storage.StorageNotCreated;

@Singleton
@Startup
public class FileStorageStartup {

	public static final String HOME_PATH = System.getProperty("user.home") + "/wildfly";
	public static final String LOKALIZATOR_PATH =  HOME_PATH + "/lokalizator";
	public static final String IMAGE_PATH = LOKALIZATOR_PATH + "/image";
	
	@PostConstruct
	public void startup(){
		createDirection(LOKALIZATOR_PATH);
		createDirection(IMAGE_PATH);
		createStorage(new AvatarImageStorage());
		
		System.out.println(HOME_PATH);
		System.out.println(LOKALIZATOR_PATH);
	}
	
	public void createStorage(FileStorage fileStorage){
		fileStorage.startup();
	}
	
	private void createDirection(String path){
		File file = new File(path);
		if(!file.exists() || !file.isDirectory()){
			boolean create = file.mkdir();
			if(!create)
				throw new StorageNotCreated("Nie udało sie utworzyc folderu dla sciezki " + path);
		} 
	}
	
}
