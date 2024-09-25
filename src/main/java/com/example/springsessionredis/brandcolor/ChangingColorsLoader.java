package com.example.springsessionredis.brandcolor;

import java.io.InputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class ChangingColorsLoader {
	
	public static ChangingColors load(InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ChangingColors.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ChangingColors changingColors = (ChangingColors) unmarshaller.unmarshal(in);
		return changingColors;
	}

}
