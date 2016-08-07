package com.pw.lokalizator.file.storage;

import java.io.File;

import com.pw.lokalizator.singleton.FileStorageStartup;

public class AvatarImageStorage implements FileStorage{
	
	public static final String AVATAR_PATH = FileStorageStartup.IMAGE_PATH + "/avatar";
	
	@Override
	public void startup() {
		File file = new File(AVATAR_PATH);

		if(!file.exists() || !file.isDirectory()){
			boolean create = file.mkdir();
			if(!create)
				throw new StorageNotCreated("Nie udało sie utworzyc folderu dla sciezki " + AVATAR_PATH);
		}
	}
}
