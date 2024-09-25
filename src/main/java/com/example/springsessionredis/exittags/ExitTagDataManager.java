package com.example.springsessionredis.exittags;

/**
 * Manager for exit tag data 
 * 
 * @author Nuance Communications
 */
public class ExitTagDataManager {
	private static ExitTagDataManager instance = null;
	private ExitTags data = null;
	
	public synchronized static ExitTagDataManager getInstance() {
		if (instance==null) {
			instance = new ExitTagDataManager();
		}
		return instance;
	}
	
	public void set(ExitTags data) {
        this.data = data;
    }
	
	public ExitTag getExitTag(String applicationTag, String brandColor) {
        if (data!=null) {
        	String tagAppTag;
        	String tagBrandColor;
            for (ExitTag tag : data.getExitTags()) {
            	tagAppTag = tag.getApplicationTag();
            	tagBrandColor = tag.getBrandColor();
            	
            	if (tagAppTag==null || tagAppTag.equals(applicationTag)) {
            		if (tagBrandColor==null || tagBrandColor.equals(brandColor)) {
            			return tag;
            		}
            	}
            }
        }
        return null;
	}
}
