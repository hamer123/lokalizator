package com.pw.lokalizator.service;

import java.awt.image.BufferedImage;

import javax.ejb.Local;
import javax.inject.Named;

@Local
public interface ImageService {
	byte[] content(String uuid);
	BufferedImage read(String uuid);
	void write(BufferedImage image, String uuid);
	void remove(String uuid);
}
