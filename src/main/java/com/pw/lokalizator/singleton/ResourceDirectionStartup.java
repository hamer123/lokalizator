package com.pw.lokalizator.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ResourceDirectionStartup {
	@PostConstruct
	public void initializeResourcesDirections() throws IOException {
		/** Zaladuj plik propertisow */
		Properties properties = loadProperties();

		/** Odczytaj zawartosc properties i ustaw pola */
		readDirections(properties);

		/** Stworz foldery dla glownych zasobow */
		createRootDirection();

		/** Stworz poboczne foldery */
		createOtherDirections();
	}

	private Properties loadProperties() throws IOException {
		InputStream inputStream  = getClass().getClassLoader().getResourceAsStream("lokalizator.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;
	}

	private void createOtherDirections() throws IOException {
		createDirection(ResourceDirectionURI.AVATAR);
	}

	private void createRootDirection() throws IOException {
		createDirection(ResourceDirectionURI.RESOURCE_MAIN);
		createDirection(ResourceDirectionURI.IMAGE);
		createDirection(ResourceDirectionURI.VIDEO);
	}

	private void createDirection(String resourcePath) throws IOException {
		Path path = Paths.get(resourcePath);
		if(!Files.exists(path) || !Files.isDirectory(path)){
			Files.createDirectories(path);
		}
	}

	private void readDirections(Properties properties){
		//home
		String homePath = System.getProperty("user.home");
		ResourceDirectionURI.RESOURCE_MAIN = homePath + readPropertie(properties,"resource.path.home");
		//roots of resource
		ResourceDirectionURI.IMAGE = ResourceDirectionURI.RESOURCE_MAIN + readPropertie(properties,"resource.path.image");
		ResourceDirectionURI.VIDEO = ResourceDirectionURI.RESOURCE_MAIN + readPropertie(properties,"resource.path.video");
		//image
		ResourceDirectionURI.AVATAR = ResourceDirectionURI.IMAGE + readPropertie(properties,"resource.path.avatar");
	}

	private String readPropertie(Properties properties, String name){
		String value = properties.getProperty(name);
		if(value == null)
			throw new RuntimeException("There is no property '" + name + "'");
		return value;
	}

	public static class ResourceDirectionURI{
		public static String RESOURCE_MAIN;
		//roots
		public static String IMAGE;
		public static String VIDEO;
		//imgage
		public static String AVATAR;
	}
}
