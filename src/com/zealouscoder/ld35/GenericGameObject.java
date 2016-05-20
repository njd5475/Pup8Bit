package com.zealouscoder.ld35;

import java.util.Properties;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;

public class GenericGameObject implements Renderable {

	private String			type;
	private String			id;
	private Sprite			sprite;
	private Properties	properties;
	
	public GenericGameObject(String id, String type, Sprite sprite, Properties properties) {
		this.type = type;
		this.properties = properties;
		this.sprite = sprite;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public String getType() {
		return type;
	}
	
	public boolean hasSprite() {
		return sprite != null;
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

	@Override
	public GamePosition getPosition() {
		return sprite.getPosition();
	}

	@Override
	public void render(GameRenderContext rc, Game game, GameRenderer renderer) {
		renderer.render(rc, game, this);
	}

	@Override
	public GameView getView() {
		return sprite.getView();
	}

	@Override
	public int getLayer() {
		return sprite.getLayer();
	}

	public boolean check(String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}

	@Override
	public boolean isRenderable() {
		return sprite != null;
	}

    public Object get(String propKey) {
        return properties.get(propKey);
    }

		@Override
		public GameObjectBound getBounds() {
			return sprite.getBounds();
		}
}
