package com.zealouscoder.ld35;

public class ImageResource {

	private String image;

	public ImageResource(String image) {
		this.image = image;
	}
	
	public int hashCode() {
		return image.hashCode();
	}
	
	public String getIdentity() {
		return image;
	}
}
