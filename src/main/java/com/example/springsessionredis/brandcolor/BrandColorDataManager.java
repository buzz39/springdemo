package com.example.springsessionredis.brandcolor;

/**
 * Manages SMS data file
 * 
 * @author Nuance Communications
 */
public class BrandColorDataManager {
	private static BrandColorDataManager instance = null;
	private ChangingColors data = null;
	
	public synchronized static BrandColorDataManager getInstance() {
		if (instance==null) {
			instance = new BrandColorDataManager();
		}
		return instance;
	}
	
	public void set(ChangingColors data) {
        this.data = data;
    }
	
	public String getText(String numberType, String o2prodl, String hnprodl, String theme) {
        if (data!=null) {
            for (ChangingColor cc : data.getChangingColors()) {
                if (numberType.equalsIgnoreCase(cc.getNumberType())) {
                    if (o2prodl.equalsIgnoreCase(cc.getO2prodl())) {
                        if (hnprodl.equalsIgnoreCase(cc.getHnprodl())) {
                            if (theme.equalsIgnoreCase(cc.getTheme())) {
                                return cc.getColor();
                            }
                        }
                    }
                }
            }
        }
        return null;
	}
	
}
