package com.zealouscoder.ld35;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import com.zealouscoder.ld35.movement.GameEvent;
import com.zealouscoder.ld35.movement.GameEventHandler;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;
import com.zealouscoder.ld35.rendering.Updater;

public class Game extends Thread implements Renderable, GameConstants {

    private boolean                             isGameRunning         = true;
    private double                              gameClock             = 0;
    private double                              accumulator;
    private double                              lastNano              = -1;
    private Set<Updater>                        updaters              = new HashSet<Updater>();
    private String                              name;
    private ConcurrentLinkedQueue<Updater>      updaterAddQueue       = new ConcurrentLinkedQueue<Updater>();
    private ConcurrentLinkedQueue<Updater>      updaterRemoveQueue    = new ConcurrentLinkedQueue<Updater>();
    private Map<Object, Double>                 timers                = new HashMap<Object, Double>();
    private GamePosition                        anchor                = GamePosition.ANCHOR;
    private SortedMap<Integer, Set<Renderable>> byLayer               = new TreeMap<Integer, Set<Renderable>>();
    private ConcurrentLinkedQueue<Renderable>   renderableAddQueue    = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Renderable>   renderableRemoveQueue = new ConcurrentLinkedQueue<>();
    private GameView                            mapViewLayer;
    private ConcurrentLinkedQueue<GameEvent>    eventQueue            = new ConcurrentLinkedQueue<GameEvent>();
    private Map<String, Set<GameEventHandler>>  eventHandlers         = new HashMap<String, Set<GameEventHandler>>();
    private Map<String, GenericGameObject>      namedObjects          = Collections
            .synchronizedMap(new HashMap<String, GenericGameObject>());
    private Map<String, Set<GenericGameObject>> byTypeObjects         = new HashMap<String, Set<GenericGameObject>>();
    private GameObjectBound                     bounds;
    private GameState                           currentState;
    private Object                              gcObject              = new Object();

    public Game(String name) {
        super(name);
        this.name = name;
        add((g, dt) -> {
            if(g.every(2, gcObject)) {
                System.gc();
            }
        });
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

    public void addEvent(GameEvent event) {
        eventQueue.add(event);
    }

    public void render(GameRenderContext rc, GameRenderer renderer) {
        render(rc, this, renderer);
    }

    @Override
    public void render(GameRenderContext rc, Game game, GameRenderer renderer) {
        renderer.render(rc, game);
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
        if (!eventQueue.isEmpty()) {
            consume(eventQueue, (event) -> {
                Set<GameEventHandler> handlers = eventHandlers.get(event.getType());
                if (handlers != null) {
                    for (GameEventHandler handler : handlers) {
                        handler.handle(Game.this, event);
                    }
                }
            });
        }
        for (Updater updater : updaters) {
            updater.update(this, dt);
        }
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

    public GameView getMapViewLayer() {
        if (mapViewLayer == null) {
            mapViewLayer = getView().scale(5, 5);
        }
        return mapViewLayer;
    }

    public boolean isValid(GenericGameObject go, GamePosition update) {
        for (Renderable nearby : neighbors(go, update)) {
            if (!isPassable(nearby) && collides(nearby, go, update)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPassable(Renderable nearby) {
        if (nearby instanceof GenericGameObject) {
            GenericGameObject go = (GenericGameObject) nearby;
            if (go.getProperties().containsKey("passable")) {
                return go.check("passable");
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean collides(Renderable nearby, GenericGameObject go, GamePosition update) {
        double radiusSq = go.getBounds().getRadiusSq() + nearby.getBounds().getRadiusSq();
        return nearby.getPosition().distSq(update) <= radiusSq;
    }

    public Renderable[] neighbors(GenericGameObject go, GamePosition update) {
        Set<Renderable> nearbys = new HashSet<Renderable>();
        for (Set<Renderable> rSet : byLayer.values()) {
            for (Renderable r : rSet) {
                if (isClose(r, update) && r != go) {
                    nearbys.add(r);
                }
            }
        }
        return nearbys.toArray(new Renderable[nearbys.size()]);
    }

    private boolean isClose(Renderable r, GamePosition update) {
        return update.isClose(r);
    }

    public void dispatchKeyEvent(int keyCode, String action) {
        Properties props = new Properties();
        props.put("keycode", keyCode);
        props.put("action", action);
        addEvent(new GameEvent("KeyEvent", props));
    }

    @Override
    public boolean isRenderable() {
        return this.getView() != null;
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

    @Override
    public GameObjectBound getBounds() {
        if (bounds == null) {
            bounds = new GameObjectBound(getView().getWidth(), getView().getHeight());
        }
        return bounds;
    }
}
