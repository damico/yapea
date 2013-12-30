package org.jdamico.yapea.commons;

public class ImageItem {
	
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

	public ImageItem(String imageName, String imageId) {
		super();
		this.imageName = imageName;
		this.imageId = imageId;
	}
	
	@Override
    public String toString() {
            return imageName;
    }
	

}
