package com.zealouscoder.ld35;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class InputSystem {

	private Game game;

	public InputSystem(Game game) {
		this.game = game;
	}

	public void init(JFrame frame) {
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				game.dispatchKeyEvent(e.getKeyCode(), "pressed");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				game.dispatchKeyEvent(e.getKeyCode(), "released");
			}

		});
	}
}
