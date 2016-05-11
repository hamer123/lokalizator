package com.pw.lokalizator.utilitis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.logging.Logger;

public class PropertiesReader {
	private Logger log = Logger.getLogger(PropertiesReader.class);
	
	private Properties prop;
	private InputStream input;
	private String propertiesName;
	
	public PropertiesReader(String propName){
		prop = new Properties();
		propertiesName = propNameSuffixAutoComplete(propName);

	}
	/**
	 * Sprawdza czy nazwa properties file zawiera suffix '.properties' i zwraca z suffixem
	 * @param propName
	 * @return
	 */
	private String propNameSuffixAutoComplete(String propName){
		if(!propName.endsWith(".properties"))
			return propName + ".properties";
		return propName;
	}
	
	/**
	 * Proba zaladowania zawartosci pliku properties
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void loadProperitesFile() throws IOException, FileNotFoundException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		input = loader.getResourceAsStream(propertiesName);
		prop.load(input);
	}
	
	/**
	 * Zwraca String jesli istnieje property o danej nazwie
	 * @param name
	 * @return
	 */
	public String findPropertyByName(String property){
		try{
			
			loadProperitesFile();
			return prop.getProperty(property);
			
		}  catch (FileNotFoundException e) {
			log.error("nie udalo sie odnalez properties " + propertiesName + ": "+ e.getMessage());
		} catch (IOException e) {
			log.error("nie udana proba odczytu " + propertiesName + ": "  + e.getMessage());
		}
		
		return null;
	}
}
