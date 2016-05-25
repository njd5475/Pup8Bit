package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.rendering.GameView;

public class ImmutablePosition extends GamePosition {

	public ImmutablePosition(double x, double y, GameView view) {
		super(x, y, view);
	}

	@Override
	public synchronized void visit(double dt, Game game, GenericGameObject go,
			PositionManipulator manipulator) {
		
	}

	
	
}
