package com.zealouscoder.ld35;

import java.util.HashMap;
import java.util.Map;

public class ImageResource {

	public static final ImageResource					PLACEHOLDER	= wrap("PLACEHOLDER");

	private static Map<String, ImageResource>	resources		= new HashMap<>();

	private String														image;

	public ImageResource(String image) {
		this.image = image;
	}

	public int hashCode() {
		return image.hashCode();
	}

	public String getIdentity() {
		return image;
	}

	public static synchronized ImageResource wrap(String name) {
		ImageResource res = resources.get(name);
		if (res == null) {
			res = new ImageResource(name);
			resources.put(name, res);
		}
		return res;
	}
}
