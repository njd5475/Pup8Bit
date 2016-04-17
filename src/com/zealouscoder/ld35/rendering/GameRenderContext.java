package com.zealouscoder.ld35.rendering;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImageOp;

import com.zealouscoder.ld35.Sprite;

public class GameRenderContext {

	private Graphics2D g;

	private GameRenderContext() {
	}

	public static GameRenderContext wrap(Graphics2D g) {
		GameRenderContext rc = new GameRenderContext();
		rc.g = g;
		return rc;
	}

	public Graphics2D getGraphics2D() {
		return g;
	}

	public void toView(GameView view) {
		g.translate(view.getX(), view.getY());
		g.scale(view.getScaleX(), view.getScaleY());
		g.rotate(view.getRotation());
		g.clipRect(0, 0, (int) view.getWidth(), (int) view.getHeight());
	}

	public void toPosition(Positioned positioned) {
		GamePosition pos = positioned.getPosition();
		toView(pos.getView());
		g.translate(pos.getX(), pos.getY());
	}

	void drawAsText(int i, int j, String format) {
		g.setColor(Color.white);
		g.drawString(format, i, j + 20);
	}

	public void drawSprite(Sprite sprite) {
		toPosition(sprite);
		g.setComposite(AlphaComposite.SrcOver);
		g.drawImage(sprite.getImage(), 0, 0, sprite.getWidth(), sprite.getHeight(),
				null);
	}
	
	protected void finalize() {
		g.dispose();
	}
	
	public GameRenderContext clone() {
		return wrap((Graphics2D) g.create());
	}
}
