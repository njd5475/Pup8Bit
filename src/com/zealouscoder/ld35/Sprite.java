package com.zealouscoder.ld35;

import java.awt.Image;

import com.zealouscoder.ld35.rendering.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.RelativePosition;
import com.zealouscoder.ld35.rendering.Renderable;

public class Sprite implements Renderable {

	private ImageResource	image;
	private GamePosition	position;
	private int						height;
	private int						width;

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
	public void render(GameRenderContext rc, GameRenderer renderer) {
		renderer.render(rc, this);
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

}
