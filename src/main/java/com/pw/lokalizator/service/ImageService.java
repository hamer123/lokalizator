package com.pw.lokalizator.service;

import java.awt.image.BufferedImage;

import javax.ejb.Local;
import javax.inject.Named;

@Local
public interface ImageService extends ResourceService<String, BufferedImage> {
	byte[] content(String uuid);
}
