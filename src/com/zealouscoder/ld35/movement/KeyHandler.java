package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.rendering.Updater;

public class KeyHandler {

	private boolean	pressed	= false;
	private Updater	updater;

	public KeyHandler(Updater updater) {
		this.updater = updater;
	}

	public void keyPressed(Game game) {
		if (!pressed) {
			pressed = true;
			game.add(updater);
		}
	}

	public void keyReleased(Game game) {
		if(pressed) {
			game.remove(updater);
			pressed = false;			
		}
	}
}
