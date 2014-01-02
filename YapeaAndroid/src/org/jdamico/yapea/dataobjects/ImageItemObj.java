package org.jdamico.yapea.dataobjects;

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

public class ImageItemObj {
	
	private String imageName = null;
	private String imageId = null;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public ImageItemObj(String imageName, String imageId) {
		super();
		this.imageName = imageName;
		this.imageId = imageId;
	}
	
	@Override
    public String toString() {
            return imageName;
    }
	

}
