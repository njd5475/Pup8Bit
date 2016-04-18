package com.zealouscoder.ld35.movement;

import static com.zealouscoder.ld35.GameObjectProps.*;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;

public class CoordinateManipulator implements PositionManipulator {

	private boolean isX;

	public CoordinateManipulator(boolean x) {
		this.isX = x;
	}

	@Override
	public GamePosition update(double dt, GenericGameObject go, Game game) {
		GamePosition pos = go.getPosition();
		double speed = go.getDouble(SPEED);
		double direction = Math.toRadians(go.getDouble(DIRECTION));
		double dx = dt * Math.cos(direction) * speed;
		double dy = dt * Math.sin(direction) * speed;
		if (isX) {
			dy = 0;
		} else if (!isX) {
			dx = 0;
		}
		return GamePosition.wrap(pos.getX() + dx, pos.getY() + dy, pos.getView());
	}

}
