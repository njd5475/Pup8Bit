package com.zealouscoder.ld35.rendering;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.ImageResource;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.movement.Positioned;

public class GameRenderContext {

    private Graphics2D                g;
    private Map<ImageResource, Image> images = new HashMap<ImageResource, Image>();

    private GameRenderContext() {
    }

    public void toView(GameView view) {
        g.setTransform(view.getTransform());
    }

    public void toPosition(Positioned positioned) {
        GamePosition pos = positioned.getPosition();
        toView(pos.getView());
        g.translate(pos.getX(), pos.getY());
    }

    void drawAsText(int i, int j, String format) {
        g.setColor(Color.white);
        g.drawString(format, i, j + 20);
    }

    public void drawSprite(Sprite sprite) {
        toPosition(sprite);
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(images.get(sprite.getImage()), 0, 0, sprite.getWidth(), sprite.getHeight(), null);
    }

    public void drawDebug(Renderable r, Color override) {
        GameObjectBound bounds = r.getBounds();
        boolean passable = true;
        if (r instanceof GenericGameObject) {
            GenericGameObject go = (GenericGameObject) r;
            if (go.getProperties().containsKey("passable")) {
                passable = go.check("passable");
            }
        }
        if (passable) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.red);
        }
        if (override != null) {
            g.setColor(override);
        }
        g.drawRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    protected void finalize() {
        g.dispose();
    }

    public GameRenderContext clone() {
        return wrap(this, (Graphics2D) g.create());
    }

    public static GameRenderContext wrap(Graphics2D g) {
        GameRenderContext rc = new GameRenderContext();
        rc.images = rc.images;
        rc.g = g;
        return rc;
    }

    public static GameRenderContext wrap(GameRenderContext context, Graphics2D g) {
        GameRenderContext rc = new GameRenderContext();
        rc.images = context.images;
        rc.g = g;
        return rc;
    }

    private static Image adjustImage(Image image) {
        return adjustImage(image, getConfig());
    }

    private static Image adjustImage(Image image, GraphicsConfiguration dc) {
        BufferedImage newImage = dc.createCompatibleImage(image.getWidth(null), image.getHeight(null),
                Transparency.TRANSLUCENT);
        Graphics g = newImage.createGraphics();
        g.clearRect(0, 0, newImage.getWidth(), newImage.getHeight());
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static GraphicsConfiguration getConfig() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    private Image loadImage(String name) {
        InputStream res = this.getClass().getClassLoader().getResourceAsStream(name);
        try {
            return ImageIO.read(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadImageResource(ImageResource res, Image image) {
        images.put(res, adjustImage(image));
    }

    public ImageResource loadImageResource(String id) {
        ImageResource res = ImageResource.wrap(id);
        if (!images.containsKey(res)) {
            Image image = loadImage(id);
            if (image == null) {
                res = null;
            } else {
                loadImageResource(res, image);
            }
        }
        return res;
    }

}
