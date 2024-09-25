package com.example.springsessionredis.sms;

/**
 * Manages SMS data file
 * 
 * @author Nuance Communications
 */
public class SMSDataManager {
	private static SMSDataManager instance = null;
	private SMSData data = null;
	
	public synchronized static SMSDataManager getInstance() {
		if (instance==null) {
			instance = new SMSDataManager();
		}
		return instance;
	}

	public void set(SMSData data) {
		this.data = data;
	}
	
	public String getText(String applicationTag, String brandColor) {
		if (data!=null) {
			for (SMS sms : data.getSms()) {
				if (applicationTag.equals(sms.getApplicationTag())) {
					if (sms.getBrandColor()==null || sms.getBrandColor().isEmpty()) {
						return sms.getText();
					}
					else if (brandColor.equals(sms.getBrandColor())) {
						return sms.getText();
					}
				}
			}
		}
		return null;
	}
	
}
