package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;

public interface GameEventHandler {

	public void handle(Game game, GameEvent event);
	
}
