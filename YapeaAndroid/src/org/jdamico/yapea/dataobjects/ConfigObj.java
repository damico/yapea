package org.jdamico.yapea.dataobjects;

public class ConfigObj {

	private String encAlgo = null;
	private String hashedKey = null;
	private boolean isGpsEnabled = false;
	
	public ConfigObj(String encAlgo, String hashedKey, boolean isGpsEnabled) {
		super();
		this.encAlgo = encAlgo;
		this.hashedKey = hashedKey;
		this.isGpsEnabled = isGpsEnabled;
	}
	public ConfigObj() {}
	public String getEncAlgo() {
		return encAlgo;
	}
	public void setEncAlgo(String encAlgo) {
		this.encAlgo = encAlgo;
	}
	public String getHashedKey() {
		return hashedKey;
	}
	public void setHashedKey(String hashedKey) {
		this.hashedKey = hashedKey;
	}
	public boolean isGpsEnabled() {
		return isGpsEnabled;
	}
	public void setGpsEnabled(boolean isGpsEnabled) {
		this.isGpsEnabled = isGpsEnabled;
	}
	
	
	
	
}
