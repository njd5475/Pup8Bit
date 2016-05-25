package com.zealouscoder.ld35;

import com.zealouscoder.ld35.builder.SpriteBuilder;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.movement.RelativePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;

public class Sprite implements Renderable {

	public static final Sprite PLACEHOLDER = SpriteBuilder.buildPlaceholder();
	private ImageResource		image;
	private GamePosition		position;
	private int							height;
	private int							width;
	private GameObjectBound	bounds;

	public Sprite(ImageResource image, int width, int height,
			GamePosition position) {
		this.image = image;
		this.position = position;
		this.width = width;
		this.height = height;
	}

	public ImageResource getImage() {
		return this.image;
	}

	@Override
	public void render(GameRenderContext rc, Game game, GameRenderer renderer) {
		renderer.render(rc, game, this);
	}

	@Override
	public GamePosition getPosition() {
		return this.position;
	}

	public GamePosition getCenterPosition() {
		return RelativePosition.wrap(getWidth() / 2, getHeight() / 2, position);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getSquaredRadius() {
		return Math.pow(width / 2, 2) + Math.pow(height / 2, 2);
	}

	@Override
	public GameView getView() {
		return position.getView();
	}

	@Override
	public int getLayer() {
		return position.getView().getLayer();
	}

	@Override
	public boolean isRenderable() {
		return true;
	}

	@Override
	public GameObjectBound getBounds() {
		if (bounds == null) {
			bounds = new GameObjectBound(width, height);
		}
		return bounds;
	}

	@Override
	public int compareTo(Renderable o) {
		int layerDiff = getLayer() - o.getLayer();
		if(layerDiff == 0) {
			return getPosition().compareTo(o.getPosition());
		}
		return layerDiff;
	}

}
