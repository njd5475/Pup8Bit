package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.Sprite;

public class DirectionUpdater extends PositionUpdater {

	private double direction;
	private double speed;

	public DirectionUpdater(Sprite sprite, double direction, double speed) {
		super(sprite);
		this.direction = Math.toRadians(direction);
		this.speed = speed;
	}

	@Override
	public void update(Sprite sprite, double dt, Game g) {
		double dx = dt * Math.cos(direction) * speed;
		double dy = dt * Math.sin(direction) * speed;
		amountX = dx;
		amountY = dy;
	}

}
