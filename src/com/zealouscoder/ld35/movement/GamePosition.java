package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;

public class GamePosition implements Comparable<GamePosition> {

	public static final GamePosition	ANCHOR	= wrapImmutable(0, 0,
			GameView.ANCHOR);
	private volatile double						x				= 0;
	private volatile double						y				= 0;
	private GameView									view;

	protected GamePosition(double x, double y, GameView view) {
		this.view = view;
		this.x = x;
		this.y = y;
	}

	private static GamePosition wrapImmutable(int i, int j, GameView view) {
		//TODO: remove dependence on subclass
		return new ImmutablePosition(i, j, view);
	}

	public synchronized void visit(double dt, Game game, GenericGameObject go,
			PositionManipulator manipulator) {
		if (!game.isValid(go, this)) {
			// should never ever get here
			throw new IllegalStateException("Invalid values are " + x + " and " + y);
			// need to find a valid position, last valid maybe?
		}
		GamePosition update = manipulator.update(dt, go, game);
		double nx = update.getX();
		double ny = update.getY();
		GamePosition newPos = GamePosition.wrap(nx, ny, view);
		if (game.isValid(go, newPos)) {
			this.x = nx;
			this.y = ny;
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
		return wrap(getX(), getY(), view);
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
		return distSq(r.getPosition()) < (r.getBounds().getRadiusSq() * 10d);
	}

	public double distSq(GamePosition update) {
		double dx = (getX() - update.getX());
		double dy = (getY() - update.getY());
		return dx * dx + dy * dy;
	}

	@Override
	public int compareTo(GamePosition o) {
		double yDiff = getY() - o.getY();
		if (yDiff == 0) {
			double xDiff = getX() - o.getX();
			if (xDiff == 0) {
				return 0;
			}
			return (int) xDiff;
		}
		return (int) yDiff;
	}
}
