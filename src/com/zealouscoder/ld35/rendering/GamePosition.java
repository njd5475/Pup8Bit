package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.movement.PositionUpdater;

public class GamePosition {

	public static final GamePosition ANCHOR = wrap(0,0,GameView.ANCHOR);
	private double	x	= 0;
	private double	y	= 0;
	private GameView		view;

	protected GamePosition(double x, double y, GameView view) {
		this.view = view;
		this.x = x;
		this.y = y;
	}

	public void visit(PositionUpdater updater) {
		this.x += updater.getX();
		this.y += updater.getY();
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public GameView getView() {
		return view;
	}
	
	public int getLayer() {
		return view.getLayer();
	}

	public static GamePosition wrap(double x, double y, GameView view) {
		return new GamePosition(x, y, view);
	}
}
