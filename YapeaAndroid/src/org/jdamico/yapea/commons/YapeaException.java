package org.jdamico.yapea.commons;

public class YapeaException extends Exception {

	private static final long serialVersionUID = -9217036014255738309L;
	
	public YapeaException(){
		super();
	}
	
	public YapeaException(Exception e){
		super(e);
	}
	
	public YapeaException(String message){
		super(message);
	}
	
	public YapeaException(String message, Exception e){
		super(message, e);
	}

}
