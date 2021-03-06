package com.zealouscoder.ld35;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import com.zealouscoder.ld35.movement.CoordinateManipulator;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.SwingRenderer;
import com.zealouscoder.ld35.scriptextension.ScriptExtension;
import com.zealouscoder.ld35.scriptextension.ScriptExtensionCoffee;

public class Pup8Bit implements GameConstants {

    private static GameRenderContext     context       = GameRenderContext.wrap(null);

    private static final SwingRenderer   renderer      = new SwingRenderer();
    private static final Game            game          = new Game(NAME, context, renderer);
    private static boolean               avatarCreated = false;
    private static final ScriptExtension scripts       = new ScriptExtensionCoffee(game);

    static {
        System.setProperty("sun.java2d.opengl", "True");
    }

    public static void main(String[] args) throws IOException {
        game.start();
        try {
            scripts.runInitScript();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        CoordinateManipulator avatarControllerX = new CoordinateManipulator(true);
        CoordinateManipulator avatarControllerY = new CoordinateManipulator(false);
        // TODO: extract into script
        game.add((g, dt) -> {
            if (!avatarCreated) {
                GenericGameObject gp = game.getFirst("spawn", "avatar");
                GamePosition pos = (GamePosition) gp.get("SpawnPosition");
                System.out.println("Spawning at " + pos.getX() + " pos " + pos.getY());
                GenericGameObject avatar = spawnPlayerAt(pos.getX(), pos.getY());
                game.add(avatar);
                avatarCreated = true;
            }
            if (avatarCreated) {
                GenericGameObject avatar = game.get("avatar");
                if (avatar != null) {
                    GamePosition p = avatar.getPosition();
                    p.visit(dt, g, avatar, avatarControllerX);
                    p.visit(dt, g, avatar, avatarControllerY);
                }
            }
        });

    }

    private static GenericGameObject spawnPlayerAt(double x, double y) {
        InputStream stream = Pup8Bit.class.getClassLoader().getResourceAsStream("puppy.png");
        BufferedImage img;
        try {
            img = ImageIO.read(stream);
            Sprite sprite = new Sprite(game.loadImageResource("avatar", img), img.getWidth() - 1, img.getHeight() - 1,
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

}
