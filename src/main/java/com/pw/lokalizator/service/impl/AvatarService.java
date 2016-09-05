package com.pw.lokalizator.service.impl;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.transaction.Transactional;
import com.pw.lokalizator.service.ImageService;
import com.pw.lokalizator.singleton.ResourceDirectionStartup;

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
	public BufferedImage create(String s, BufferedImage bufferedImage) {
		return null;
	}

	@Override
	public BufferedImage update(String s, BufferedImage bufferedImage) {
		return null;
	}

	@Override
	public void remove(String uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BufferedImage find(String s) {
		return null;
	}

	private String path(String uuid){
		return  ResourceDirectionStartup.ResourceDirectionURI.AVATAR
				+ "/" 
				+ uuid ;
	}

}
