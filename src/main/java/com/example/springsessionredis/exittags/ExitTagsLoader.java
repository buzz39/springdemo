package com.example.springsessionredis.exittags;

import java.io.InputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Loader for the exit tags.
 *  
 * @author Nuance Communications
 */
public class ExitTagsLoader {
	
	public static ExitTags load(InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ExitTags.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ExitTags exitTags = (ExitTags) unmarshaller.unmarshal(in);
		return exitTags;
	}

}

