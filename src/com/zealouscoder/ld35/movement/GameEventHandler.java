package com.zealouscoder.ld35.movement;

import java.util.Properties;

import com.zealouscoder.ld35.Game;

public interface GameEventHandler {

	public Properties handle(Game game, GameEvent event);
	
}
