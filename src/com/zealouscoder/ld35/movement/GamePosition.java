package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;

public class GamePosition {

	public static final GamePosition	ANCHOR	= wrap(0, 0, GameView.ANCHOR);
	private double										x				= 0;
	private double										y				= 0;
	private GameView									view;

	protected GamePosition(double x, double y, GameView view) {
		this.view = view;
		this.x = x;
		this.y = y;
	}

	public void visit(double dt, Game game, GenericGameObject go,
			PositionManipulator manipulator) {
		GamePosition update = manipulator.update(dt, go, game);
		if (game.isValid(go, update)) {
			this.x = update.x;
			this.y = update.y;
			this.view = update.view;
		}
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

	public GamePosition clone() {
		return wrap(x, y, view);
	}

	public static GamePosition wrap(double x, double y, GameView view) {
		return new GamePosition(x, y, view);
	}

	/**
	 * Rough estimate of distance without taking into account details
	 * 
	 * @param update
	 * @return
	 */
	public boolean isClose(Renderable r) {
		return distSq(r.getPosition()) < (r.getBounds().getRadiusSq()*10);
	}

	public double distSq(GamePosition update) {
		double dx = (x - update.getX());
		double dy = (y - update.getY());
		return dx * dx + dy * dy;
	}
}
