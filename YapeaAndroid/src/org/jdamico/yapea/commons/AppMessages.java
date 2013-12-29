package org.jdamico.yapea.commons;

public class AppMessages {
	
	private static AppMessages INSTANCE = null;
	
	private AppMessages(){}
	
	public static AppMessages getInstance(){
		if(null == INSTANCE) INSTANCE = new AppMessages();
		return INSTANCE;
	}
	
	public String getMessage(String messageId){
		
		return messageId;
		
	}

}
