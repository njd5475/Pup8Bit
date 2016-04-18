package com.zealouscoder.ld35.rendering.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Properties;

import javax.swing.JComponent;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GameConstants;
import com.zealouscoder.ld35.movement.GameEvent;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;

public class GameComponent extends JComponent implements GameConstants {

	private BufferedImage			bkBuff	= new BufferedImage(GAME_WIDTH, GAME_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);
	private Game							game;
	private GameRenderContext	context;
	private GameRenderer			renderer;
	private Object						drawing	= new Object();

	{
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		setBackground(new Color(0, 0, 0, 0));
	}

	public GameComponent(Game game, GameRenderContext context,
			GameRenderer renderer) {
		this.game = game;
		this.context = context;
		this.renderer = renderer;
		game.addEventHandler("repaint", (g, event) -> {
			synchronized (drawing) {
				// clear and draw the backbuffer
				Graphics2D bkg = bkBuff.createGraphics();
				bkg.setColor(new Color(0, 0, 0, 255));
				bkg.fillRect(0, 0, bkBuff.getWidth(), bkBuff.getHeight());
				g.render(GameRenderContext.wrap(context, (Graphics2D) bkg), renderer);
				drawing.notifyAll();
			}
			repaint();
		});
	}

	public void paintComponent(Graphics init) {
		synchronized (drawing) {
			Graphics2D g = (Graphics2D) init.create();
			g.drawImage(bkBuff, 0, 0, null);
			g.dispose();

			drawing.notifyAll();
		}
	}

}
