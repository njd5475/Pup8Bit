package com.zealouscoder.ld35.builder;

import java.util.Properties;

import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.Sprite;

public class GameObjectBuilder {

	private String strId;
	private String strType;
	private Sprite sprite;
	private Properties properties;
	
	public GameObjectBuilder() {
		properties = new Properties();
		sprite = Sprite.PLACEHOLDER;
	}

	public GenericGameObject build() {
		
		return new GenericGameObject(strId, strType, sprite, properties);
	}
}
