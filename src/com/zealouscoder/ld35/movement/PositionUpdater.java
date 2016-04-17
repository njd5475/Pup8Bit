package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.rendering.Updater;

public abstract class PositionUpdater implements Updater {

	private Sprite sprite;
	protected double amountX = 0;
	protected double amountY = 0;

	public PositionUpdater(Sprite sprite) {
		this.sprite = sprite;
	}
	
	@Override
	public void update(Game g, double dt) {
		update(sprite, dt, g);
		sprite.getCenterPosition().visit(this);
	}

	public abstract void update(Sprite sprite, double dt, Game g);

	public double getX() {
		return amountX;
	}

	public double getY() {
		return amountY;
	}

}
