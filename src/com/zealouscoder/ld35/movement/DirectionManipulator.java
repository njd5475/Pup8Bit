package com.zealouscoder.ld35.movement;

import static com.zealouscoder.ld35.GameObjectProps.*;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;

public class DirectionManipulator implements PositionManipulator {

	@Override
	public GamePosition update(double dt, GamePosition pos, GenericGameObject go,
			Game game) {
		double speed = go.getDouble(SPEED);
		double direction = go.getDouble(DIRECTION);
		double dx = dt * Math.cos(direction) * speed;
		double dy = dt * Math.sin(direction) * speed;
		return GamePosition.wrap(pos.getX() + dx, pos.getY() + dy, pos.getView());
	}

}
