package com.zealouscoder.ld35;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.zealouscoder.ld35.movement.DirectionUpdater;
import com.zealouscoder.ld35.movement.KeyHandler;
import com.zealouscoder.ld35.movement.PositionUpdater;

public class InputSystem {

	private static final Map<Integer, KeyHandler>	keyHandlers	= new HashMap<Integer, KeyHandler>();

	private Sprite																avatar;

	private Game																	game;

	public InputSystem(Sprite avatar, Game game) {
		this.avatar = avatar;
		this.game = game;
	}

	public void init(JFrame frame) {
		double speed = 5;
		PositionUpdater downU = new DirectionUpdater(avatar, 90, speed);
		PositionUpdater upU = new DirectionUpdater(avatar, 270, speed);
		PositionUpdater leftU = new DirectionUpdater(avatar, 180, speed);
		PositionUpdater rightU = new DirectionUpdater(avatar, 0, speed);
		keyHandlers.put(KeyEvent.VK_DOWN, new KeyHandler(downU));
		keyHandlers.put(KeyEvent.VK_UP, new KeyHandler(upU));
		keyHandlers.put(KeyEvent.VK_LEFT, new KeyHandler(leftU));
		keyHandlers.put(KeyEvent.VK_RIGHT, new KeyHandler(rightU));
		keyHandlers.put(KeyEvent.VK_ESCAPE, new KeyHandler((g, dt) -> {
			g.quit();
		}));
		initInputSystem(frame);
	}

	private void initInputSystem(JFrame jframe) {
		jframe.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (keyHandlers.containsKey(e.getKeyCode())) {
					keyHandlers.get(e.getKeyCode()).keyPressed(game);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (keyHandlers.containsKey(e.getKeyCode())) {
					keyHandlers.get(e.getKeyCode()).keyReleased(game);
				}
			}

		});
	}
}
