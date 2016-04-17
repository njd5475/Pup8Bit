package com.zealouscoder.ld35.rendering;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.zealouscoder.ld35.ImageResource;
import com.zealouscoder.ld35.Sprite;

public class GameRenderContext {

	private Graphics2D								g;
	private Map<ImageResource, Image>	images	= new HashMap<ImageResource, Image>();

	private GameRenderContext() {
	}

	public ImageResource loadImageResource(String id, Image image) {
		ImageResource imageRes = new ImageResource(id);
		images.put(imageRes, adjustImage(image));
		return imageRes;
	}

	public static GameRenderContext wrap(Graphics2D g) {
		GameRenderContext rc = new GameRenderContext();
		rc.images = rc.images;
		rc.g = g;
		return rc;
	}

	public static GameRenderContext wrap(GameRenderContext context,
			Graphics2D g) {
		GameRenderContext rc = new GameRenderContext();
		rc.images = context.images;
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
		g.drawImage(images.get(sprite.getImage()), 0, 0, sprite.getWidth(),
				sprite.getHeight(), null);
	}

	protected void finalize() {
		g.dispose();
	}

	public GameRenderContext clone() {
		return wrap(this, (Graphics2D) g.create());
	}

	private static Image adjustImage(Image image) {
		return adjustImage(image, getConfig());
	}

	private static Image adjustImage(Image image, GraphicsConfiguration dc) {
		BufferedImage newImage = dc.createCompatibleImage(image.getWidth(null),
				image.getHeight(null), Transparency.TRANSLUCENT);
		Graphics g = newImage.createGraphics();
		g.clearRect(0, 0, newImage.getWidth(), newImage.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static GraphicsConfiguration getConfig() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
	}
}
