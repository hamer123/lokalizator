package com.pw.lokalizator.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.pw.lokalizator.file.storage.AvatarImageStorage;
import com.pw.lokalizator.service.ImageService;

@Named
@ApplicationScoped
@Transactional
public class AvatarService implements ImageService{
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public byte[] content(String uuid) {
		String path = path(uuid);
		try {
			return Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BufferedImage read(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(BufferedImage image, String uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String uuid) {
		// TODO Auto-generated method stub
		
	}
	
	private String path(String uuid){
		return    AvatarImageStorage.AVATAR_PATH 
				+ "/" 
				+ uuid ;
	}

}
