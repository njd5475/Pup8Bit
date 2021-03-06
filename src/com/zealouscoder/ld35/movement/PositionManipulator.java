package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;

public interface PositionManipulator {

	public GamePosition update(double dt, GenericGameObject go, Game game);

}
