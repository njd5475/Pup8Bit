package com.zealouscoder.ld35.rendering;

import java.awt.geom.AffineTransform;

import com.zealouscoder.ld35.GameConstants;

public class GameView implements GameConstants {

	public static final GameView	ANCHOR	= new GameView(0, 0, 1, 1, 0,
			GAME_WIDTH, GAME_HEIGHT, 0);
	private double								startx;
	private double								starty;
	private double								scaleX;
	private double								scaleY;
	private double								rot;
	private double								width;
	private double								height;
	private int										layer;
    private AffineTransform transform;

	protected GameView(double startx, double starty, double scaleX, double scaleY,
			double rot, double width, double height, int layer) {
		this.startx = startx;
		this.starty = starty;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.rot = rot;
		this.width = width;
		this.height = height;
		this.layer = layer;
	}
	
	public AffineTransform getTransform() {
	    if(transform == null) {
    		transform = new AffineTransform();
    		transform.scale(scaleX, scaleY);
    		transform.rotate(rot);
    		transform.translate(startx, starty);
	    }
		return transform;
	}

    public double getX() {
		return startx;
	}

	public double getY() {
		return starty;
	}

	public double getScaleX() {
		return scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public double getRotation() {
		return rot;
	}

	public int getLayer() {
		return layer;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	public double getRadiusSq() {
		double halfW = width/2d;
		double halfH = height/2d;
		return halfW*halfW + halfH * halfH;
	}

	public static ViewBuilder getBuilder(GameView view) {
		return getBuilder().copyView(view);
	}
	
	public static ViewBuilder getBuilder() {
		return new ViewBuilder(new ViewBuilder() {
			private GameView newView = new GameView(0, 0, 1, 1, 0, GAME_WIDTH,
					GAME_HEIGHT, 0);

			@Override
			ViewBuilder copyView(GameView cpy) {
				newView.height = cpy.height;
				newView.width = cpy.width;
				newView.layer = cpy.layer;
				newView.rot = cpy.rot;
				newView.scaleX = cpy.scaleX;
				newView.scaleY = cpy.scaleY;
				newView.startx = cpy.startx;
				newView.starty = cpy.starty;
				return this;
			}
			
			@Override
			public ViewBuilder size(double width, double height) {
				newView.width = width;
				newView.height = height;
				return this;
			}

			@Override
			public ViewBuilder start(double x, double y) {
				newView.startx = x;
				newView.starty = y;
				return this;
			}

			@Override
			public ViewBuilder scale(double x, double y) {
				newView.scaleX = x;
				newView.scaleY = y;
				return this;
			}

			@Override
			public ViewBuilder rotation(double rot) {
				newView.rot = rot;
				return this;
			}

			@Override
			public ViewBuilder upALayer() {
				newView.layer++;
				return this;
			}
			
			@Override
			public ViewBuilder setLayer(int l) {
				newView.layer = l;
				return this;
			}

			@Override
			public GameView build() {
				return newView;
			}

		});
	}

	public GameView upALayer() {
		return getBuilder(this).upALayer().build();
	}

	public GameView forLayer(int l) {
		return getBuilder(this).setLayer(l).build();
	}

	public GameView scale(double scaleX, double scaleY) {
		return getBuilder(this).scale(scaleX, scaleY).build();
	}
}
