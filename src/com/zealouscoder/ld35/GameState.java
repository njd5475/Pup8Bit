package com.zealouscoder.ld35;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import com.zealouscoder.ld35.movement.GameEvent;
import com.zealouscoder.ld35.movement.GameEventHandler;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.partitioning.IntegerBinPartitionStrategy;
import com.zealouscoder.ld35.partitioning.SpacePartitionStrategy;
import com.zealouscoder.ld35.rendering.Renderable;
import com.zealouscoder.ld35.rendering.Updater;

public class GameState {

	private ConcurrentLinkedQueue<Updater>			updaterAddQueue				= new ConcurrentLinkedQueue<Updater>();
	private ConcurrentLinkedQueue<Updater>			updaterRemoveQueue		= new ConcurrentLinkedQueue<Updater>();
	private ConcurrentLinkedQueue<Renderable>		renderableAddQueue		= new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Renderable>		renderableRemoveQueue	= new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<GameEvent>		eventQueue						= new ConcurrentLinkedQueue<GameEvent>();

	private Map<String, Set<GameEventHandler>>	eventHandlers					= new HashMap<String, Set<GameEventHandler>>();
	private Map<String, GenericGameObject>			namedObjects					= Collections
			.synchronizedMap(new HashMap<String, GenericGameObject>());
	private Map<String, Set<GenericGameObject>>	byTypeObjects					= new HashMap<String, Set<GenericGameObject>>();
	private Map<Object, Double>									timers								= new HashMap<Object, Double>();
	private SortedMap<Integer, Set<Renderable>>	byLayer								= new TreeMap<Integer, Set<Renderable>>();
	private Set<Updater>												updaters							= new HashSet<Updater>();
	private Game																game;
	private SpacePartitionStrategy							strategy							= new IntegerBinPartitionStrategy();

	public GameState(Game game) {
		if (game == null) {
			throw new NullPointerException(
					"Cannot create a game state without a game!");
		}
		this.game = game;
	}

	public void addEventHandler(String eventType, GameEventHandler handler) {
		Set<GameEventHandler> handlers = eventHandlers.get(eventType);
		if (handlers == null) {
			handlers = new HashSet<GameEventHandler>();
			eventHandlers.put(eventType, handlers);
		}
		handlers.add(handler);
	}

	public GenericGameObject[] byType(String type) {
		Set<GenericGameObject> set = byTypeObjects.get(type);
		return set.toArray(new GenericGameObject[set.size()]);
	}

	public GenericGameObject get(String key) {
		return namedObjects.get(key);
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

	public void addEvent(GameEvent event) {
		eventQueue.add(event);
	}

	public void update(double dt) {
		if (!eventQueue.isEmpty()) {
			consume(eventQueue, (event) -> {
				Set<GameEventHandler> handlers = eventHandlers.get(event.getType());
				if (handlers != null) {
					for (GameEventHandler handler : handlers) {
						handler.handle(game, event);
					}
				}
			});
		}
		for (Updater updater : updaters) {
			updater.update(game, dt);
		}
		emptyQueues();
	}

	private void emptyQueues() {
		// empty waiting queue
		if (!updaterAddQueue.isEmpty()) {
			consume(updaterAddQueue, (upd) -> {
				addUpdater(upd);
			});
		}
		if (!updaterRemoveQueue.isEmpty()) {
			consume(updaterRemoveQueue, (upd) -> {
				removeUpdater(upd);
			});
		}
		if (!renderableAddQueue.isEmpty()) {
			consume(renderableAddQueue, (r) -> {
				addRenderable(r);

			});
		}
		if (!renderableRemoveQueue.isEmpty()) {
			consume(renderableRemoveQueue, (r) -> {
				removeRenderable(r);
			});
		}
	}

	private void addUpdater(Updater updater) {
		updaters.add(updater);
	}

	private void removeUpdater(Updater updater) {
		updaters.remove(updater);
	}

	private void addRenderable(Renderable r) {
		if (r.isRenderable()) {
			Set<Renderable> layer = byLayer.get(r.getLayer());
			if (layer == null) {
				layer = new HashSet<Renderable>();
				byLayer.put(r.getLayer(), layer);
			}
			layer.add(r);
		}
		if (r instanceof GenericGameObject) {
			GenericGameObject go = (GenericGameObject) r;
			namedObjects.put(go.getId(), go);
			addTypeObject(go);
		}
		strategy.add(r);
	}

	private void addTypeObject(GenericGameObject go) {
		Set<GenericGameObject> objs = byTypeObjects.get(go.getType());
		if (objs == null) {
			objs = new HashSet<GenericGameObject>();
			byTypeObjects.put(go.getType(), objs);
		}
		objs.add(go);
	}

	private void removeRenderable(Renderable r) {
		Set<Renderable> layer = byLayer.get(r.getLayer());
		if (layer != null) {
			layer.remove(r);
			if (layer.isEmpty()) {
				byLayer.remove(r.getLayer());
			}
		}
		if (r instanceof GenericGameObject) {
			GenericGameObject go = (GenericGameObject) r;
			namedObjects.remove(go.getId());
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
			next = game.getGameClock() + d;
			timers.put(anyObj, next);
		}

		if (game.getGameClock() >= next) {
			timers.put(anyObj, game.getGameClock() + d);
			return true;
		}

		return false;
	}

	public Set<Entry<Integer, Set<Renderable>>> getRenderables() {
		return byLayer.entrySet();
	}

	public Renderable[] neighbors(GenericGameObject go, GamePosition update) {
		Set<Renderable> nearbys = new HashSet<Renderable>();
		int count = 0;
		for (Renderable r : strategy.get(update, go.getBounds())) {
			++count;
			if (isClose(r, go.getPosition()) && r != go) {
				nearbys.add(r);
			}
		}
		System.out.println(count);
		return nearbys.toArray(new Renderable[nearbys.size()]);
	}

	public GenericGameObject getFirst(String propKey, String propVal) {
		GenericGameObject ret = null;
		for (Map.Entry<String, GenericGameObject> entry : namedObjects.entrySet()) {
			if (propVal.equals(entry.getValue().get(propKey))) {
				ret = entry.getValue();
				break;
			}
		}
		return ret;
	}

	private boolean isClose(Renderable r, GamePosition update) {
		return update.isClose(r);
	}
}
