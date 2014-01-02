package org.jdamico.yapea.dataobjects;

public class ConfigObj {

	private String encAlgo = null;
	private String hashedKey = null;
	private boolean isGpsEnabled = false;
	private String panicPassword = null;
	private int panicNumber = 0;
	
	public ConfigObj(String encAlgo, String hashedKey, boolean isGpsEnabled, String panicPassword, int panicNumber) {
		super();
		this.encAlgo = encAlgo;
		this.hashedKey = hashedKey;
		this.isGpsEnabled = isGpsEnabled;
		this.panicPassword = panicPassword;
		this.panicNumber = panicNumber;
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
	public String getPanicPassword() {
		return panicPassword;
	}
	public void setPanicPassword(String panicPassword) {
		this.panicPassword = panicPassword;
	}
	public int getPanicNumber() {
		return panicNumber;
	}
	public void setPanicNumber(int panicNumber) {
		this.panicNumber = panicNumber;
	}
	
	
	
	
}
