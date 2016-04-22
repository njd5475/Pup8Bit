package com.zealouscoder.ld35;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.script.ScriptException;
import javax.swing.JFrame;

import com.zealouscoder.ld35.movement.CoordinateManipulator;
import com.zealouscoder.ld35.movement.GameEvent;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.SwingRenderer;
import com.zealouscoder.ld35.rendering.swing.GameComponent;
import com.zealouscoder.ld35.scriptextension.ScriptExtension;
import com.zealouscoder.ld35.scriptextension.ScriptExtensionCoffee;

public class Pup8Bit implements GameConstants {

    private static final Game            game          = new Game(NAME);
    private static GameRenderContext     context       = GameRenderContext.wrap(null);
    private static boolean               avatarCreated = false;
    private static final MapLoader       mapLoader     = new MapLoader("resources/puplevel1.tmx", context, game);
    private static final SwingRenderer   renderer      = new SwingRenderer();
    private static final ScriptExtension scripts       = new ScriptExtensionCoffee(game);

    static {
        System.setProperty("sun.java2d.opengl", "True");
    }

    private static final InputSystem input = new InputSystem(game);

    public static void main(String[] args) throws IOException {
        game.start();
        GameComponent comp = new GameComponent(game, context, renderer);
        JFrame jframe = createWindow(comp);
        mapLoader.load();
        game.add((g, dt) -> {
            if (g.every(FPS, comp)) {
                game.addEvent(new GameEvent("repaint", new Properties()));
            }
        });

        CoordinateManipulator avatarControllerX = new CoordinateManipulator(true);
        CoordinateManipulator avatarControllerY = new CoordinateManipulator(false);

        game.add((g, dt) -> {
            if (!avatarCreated) {
                GenericGameObject gp = game.getFirst("spawn", "avatar");
                GamePosition pos = (GamePosition) gp.get("SpawnPosition");
                System.out.println("Spawning at " + pos.getX() + " pos " + pos.getY());
                GenericGameObject avatar = spawnPlayerAt(pos.getX(), pos.getY());
                game.add(avatar);
                avatarCreated = true;
            }
            GenericGameObject avatar = game.get("avatar");
            if (avatar != null) {
                avatar.getPosition().visit(dt, g, avatar, avatarControllerX);
                avatar.getPosition().visit(dt, g, avatar, avatarControllerY);
            }
        });
        input.init(jframe);
        jframe.setVisible(true);
        try {
            scripts.runInitScript();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static GenericGameObject spawnPlayerAt(double x, double y) {
        InputStream stream = Pup8Bit.class.getClassLoader().getResourceAsStream("puppy.png");
        BufferedImage img;
        try {
            img = ImageIO.read(stream);
            Sprite sprite = new Sprite(context.loadImageResource("avatar", img), img.getWidth(), img.getHeight(),
                    GamePosition.wrap(x, y, game.getMapViewLayer().forLayer(20)));
            Properties props = new Properties();
            props.put(GameObjectProps.DIRECTION.name(), 0d);
            props.put(GameObjectProps.SPEED.name(), 0d);
            return new GenericGameObject("avatar", "Player", sprite, props);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JFrame createWindow(GameComponent comp) {
        JFrame jframe = new JFrame("Pup8Bit");
        jframe.setLayout(new BorderLayout());
        jframe.add(comp);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        return jframe;
    }

}
