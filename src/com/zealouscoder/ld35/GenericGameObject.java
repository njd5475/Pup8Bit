package com.zealouscoder.ld35;

import java.util.Properties;

public class GenericGameObject {

	private String			type;
	private Sprite			sprite;
	private Properties	properties;

	public GenericGameObject(String type, Sprite sprite, Properties properties) {
		this.type = type;
		this.properties = properties;
		this.sprite = sprite;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public String getType() {
		return type;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public double getDouble(String key) {
		properties.get(key);
		return 0;
	}

	public double getDouble(GameObjectProps prop) {
		Object obj = properties.get(prop.toString());
		if(obj instanceof Number) {
			Number number = (Number) obj;
			return number.doubleValue();
		}
		return 0;
	}
}
