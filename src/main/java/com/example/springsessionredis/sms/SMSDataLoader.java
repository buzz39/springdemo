package com.example.springsessionredis.sms;

import java.io.InputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class SMSDataLoader {
	
	public static SMSData load(InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(SMSData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SMSData smsData = (SMSData) unmarshaller.unmarshal(in);
		return smsData;
	}

}
