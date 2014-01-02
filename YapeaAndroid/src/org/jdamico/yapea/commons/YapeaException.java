package org.jdamico.yapea.commons;

/*
 * This file is part of YAPEA.
 * 
 *    YAPEA is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    YAPEA is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with YAPEA.  If not, see <http://www.gnu.org/licenses/>.
 */

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
