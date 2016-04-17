package com.zealouscoder.ld35;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import com.zealouscoder.ld35.rendering.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.Renderable;
import com.zealouscoder.ld35.rendering.Updater;

public class Game extends Thread implements Renderable, GameConstants {

	private boolean															isGameRunning					= true;
	private double															gameClock							= 0;
	private double															accumulator;
	private double															lastNano							= -1;
	private Set<Updater>												updaters							= new HashSet<Updater>();
	private String															name;
	private ConcurrentLinkedQueue<Updater>			updaterAddQueue				= new ConcurrentLinkedQueue<Updater>();
	private ConcurrentLinkedQueue<Updater>			updaterRemoveQueue		= new ConcurrentLinkedQueue<Updater>();
	private Map<Object, Double>									timers								= new HashMap<Object, Double>();
	private GamePosition												anchor								= GamePosition.ANCHOR;
	private SortedMap<Integer, Set<Renderable>>	byLayer								= new TreeMap<Integer, Set<Renderable>>();
	private ConcurrentLinkedQueue<Renderable>		renderableAddQueue		= new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Renderable>		renderableRemoveQueue	= new ConcurrentLinkedQueue<>();

	public Game(String name) {
		super(name);
		this.name = name;
	}

	public String getGameName() {
		return name;
	}

	public double getGameClock() {
		return gameClock;
	}

	public void add(Updater updater) {
		updaterAddQueue.add(updater);
	}

	public void remove(Updater updater) {
		updaterRemoveQueue.add(updater);
	}

	public void add(Renderable renderable) {
		renderableAddQueue.add(renderable);
	}

	public void remove(Renderable renderable) {
		renderableRemoveQueue.add(renderable);
	}

	@Override
	public void render(GameRenderContext rc, GameRenderer renderer) {
		renderer.render(rc, this);
	}

	@Override
	public void run() {
		while (isGameRunning) {

			double nano = System.nanoTime();
			if (lastNano <= 0) {
				lastNano = nano;
			}

			double dt = (nano - lastNano) * NS_TO_SECONDS;

			lastNano = nano;

			accumulator += dt;

			// decrement fixed time step from accumulator
			while (accumulator >= UPDATE_INTERVAL) {
				accumulator -= UPDATE_INTERVAL;
				update(UPDATE_INTERVAL);
				emptyQueues();
			}
		}
		System.exit(0);
	}

	private void update(double dt) {
		gameClock += dt;
		for (Updater updater : updaters) {
			updater.update(this, dt);
		}
	}

	private void emptyQueues() {
		// empty waiting queue
		if (!updaterAddQueue.isEmpty()) {
			consume(updaterAddQueue, (upd) -> {
				updaters.add(upd);
			});
		}
		if (!updaterRemoveQueue.isEmpty()) {
			consume(updaterRemoveQueue, (upd) -> {
				updaters.remove(upd);
			});
		}
		if (!renderableAddQueue.isEmpty()) {
			consume(renderableAddQueue, (r) -> {
				Set<Renderable> layer = byLayer.get(r.getLayer());
				if (layer == null) {
					layer = new HashSet<Renderable>();
					byLayer.put(r.getLayer(), layer);
				}
				layer.add(r);
			});
		}
		if (!renderableRemoveQueue.isEmpty()) {
			consume(renderableRemoveQueue, (r) -> {
				Set<Renderable> layer = byLayer.get(r.getLayer());
				if (layer != null) {
					layer.remove(r);
					if (layer.isEmpty()) {
						byLayer.remove(r.getLayer());
					}
				}
			});
		}
	}

	private <T> void consume(Queue<T> queue, Consumer<T> consumer) {
		while (!queue.isEmpty()) {
			T poll = queue.remove();
			consumer.accept(poll);
		}
	}

	public boolean every(double d, Object anyObj) {
		Double next = timers.get(anyObj);
		if (next == null) {
			next = gameClock + d;
			timers.put(anyObj, next);
		}

		if (gameClock >= next) {
			timers.put(anyObj, gameClock + d);
			return true;
		}

		return false;
	}

	@Override
	public GamePosition getPosition() {
		return anchor;
	}

	public Set<Entry<Integer, Set<Renderable>>> getRenderables() {
		return byLayer.entrySet();
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public GameView getView() {
		return anchor.getView();
	}

	public void quit() {
		isGameRunning = false;
	}
}
