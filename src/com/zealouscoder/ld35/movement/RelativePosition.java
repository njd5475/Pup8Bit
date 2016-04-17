package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;

public class RelativePosition extends GamePosition {

	private GamePosition position;

	protected RelativePosition(double x, double y, GamePosition position) {
		super(x, y, position.getView());
		this.position = position;
	}

	@Override
	public void visit(double dt, Game game, GenericGameObject go,
			PositionManipulator manipulator) {
		position.visit(dt, game, go, manipulator);
	}

	@Override
	public double getX() {
		return super.getX() + position.getX();
	}

	@Override
	public double getY() {
		return super.getY() + position.getY();
	}

	public static GamePosition wrap(double x, double y, GamePosition pos) {
		return new RelativePosition(x, y, pos);
	}
}
