package com.zealouscoder.ld35;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
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

    private String                  name;
    private boolean                 isGameRunning = true;
    private double                  gameClock     = 0;
    private double                  accumulator;
    private double                  lastNano      = -1;
    private GamePosition            anchor        = GamePosition.ANCHOR;
    private GameObjectBound         bounds;
    private GameState               currentState;
    private Object                  gcObject      = new Object();
    private final MapLoader         mapLoader;
    private final GameRenderContext renderContext;
    private GameRenderer            renderer;
    private GameView                mapViewLayer;
    private Map<String, GameState>  states        = new HashMap<String, GameState>();

    public Game(String name, GameRenderContext context, GameRenderer renderer) {
        super(name);
        if(currentState == null) {
            currentState = createNewState("default");
        }
        this.renderer = renderer;
        this.renderContext = context;
        this.mapLoader = new MapLoader(this);
        this.name = name;
        add((g, dt) -> {
            if (g.every(1, gcObject)) {
                System.gc();
            }
        });
    }

    public void createWindow() {
        this.getRenderer().createWindow(renderContext, getName(), this);
    }

    public void set(GameRenderer renderer) {
        this.renderer = renderer;
    }

    public GameRenderer getRenderer() {
        return this.renderer;
    }

    public ImageResource loadImage(String id) {
        return renderContext.loadImageResource(id);
    }

    public boolean loadMap(String mapFile) {
        try {
            mapLoader.loadMap(mapFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addEventHandler(String eventType, GameEventHandler handler) {
        currentState.addEventHandler(eventType, handler);
    }

    public GenericGameObject[] byType(String type) {
        return currentState.byType(type);
    }

    public GenericGameObject get(String key) {
        return currentState.get(key);
    }

    public String getGameName() {
        return name;
    }

    public double getGameClock() {
        return gameClock;
    }

    public void add(Updater updater) {
        currentState.add(updater);
    }

    public void remove(Updater updater) {
        currentState.remove(updater);
    }

    public void add(Renderable renderable) {
        currentState.add(renderable);
    }

    public void remove(Renderable renderable) {
        currentState.remove(renderable);
    }

    public void addEvent(GameEvent event) {
        currentState.addEvent(event);
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
                if (!isGameRunning) {
                    break;
                }
            }
        }
        System.exit(0);
    }

    private void update(double dt) {
        gameClock += dt;
        currentState.update(dt);
    }

    private <T> void consume(Queue<T> queue, Consumer<T> consumer) {
        while (!queue.isEmpty()) {
            T poll = queue.remove();
            consumer.accept(poll);
        }
    }

    public boolean every(double d, Object anyObj) {
        return currentState.every(d, anyObj);
    }

    @Override
    public GamePosition getPosition() {
        return anchor;
    }

    public Set<Entry<Integer, Set<Renderable>>> getRenderables() {
        return currentState.getRenderables();
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
            if (!isPassable(nearby)) {
                if (collides(nearby, go, update)) {
                    return false;
                }
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
        return nearby.getBounds().collides(nearby.getPosition(), go.getBounds(), update);
    }

    public Renderable[] neighbors(GenericGameObject go, GamePosition update) {
        return currentState.neighbors(go, update);
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
        return currentState.getFirst(propKey, propVal);
    }

    @Override
    public GameObjectBound getBounds() {
        if (bounds == null) {
            bounds = new GameObjectBound(getView().getWidth(), getView().getHeight());
        }
        return bounds;
    }

    public ImageResource loadImageResource(String id, Image image) {
        ImageResource res = ImageResource.wrap(id);
        renderContext.loadImageResource(res, image);
        return res;
    }

    public GameState createNewState(String name) {
        GameState state;
        states.put(name, state = new GameState(this));
        return state;
    }
}
