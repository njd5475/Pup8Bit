package com.zealouscoder.ld35;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.zealouscoder.ld35.movement.DirectionUpdater;
import com.zealouscoder.ld35.movement.KeyHandler;
import com.zealouscoder.ld35.movement.PositionUpdater;
import com.zealouscoder.ld35.rendering.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.SwingRenderer;

import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

public class Pup8Bit implements GameConstants {

	static {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.transaccel", "True");
	}

	private static final Game						game			= new Game(NAME);
	private static final SwingRenderer	renderer	= new SwingRenderer();

	public static void main(String[] args) throws IOException {
		game.start();
		JFrame jframe = new JFrame("Pup8Bit");
		final JComponent drawPane = new JComponent() {
			private BufferedImage bkBuff = new BufferedImage(GAME_WIDTH, GAME_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);

			{
				setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
				setBackground(new Color(0, 0, 0, 0));
			}

			public void paintComponent(Graphics init) {
				Graphics2D g = (Graphics2D) init.create();
				g.drawImage(bkBuff, 0, 0, null);
				g.dispose();
				//clear and draw the backbuffer
				Graphics2D bkg = bkBuff.createGraphics();
				bkg.setColor(new Color(0, 0, 0, 255));
				bkg.fillRect(0, 0, bkBuff.getWidth(), bkBuff.getHeight());
				render(bkg);
			}
		};
		try {
			loadMaps();
		} catch (Exception e) {
			e.printStackTrace();
		}
		game.add((g, dt) -> {
			if (g.every(FPS, drawPane)) {
				drawPane.repaint();
			}
		});
		game.add(avatar);
		initInputSystem(avatar, jframe);
		jframe.setLayout(new BorderLayout());
		jframe.add(drawPane);
		jframe.pack();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
	}

	private static void loadMaps() throws Exception {
		TMXMapReader reader = new TMXMapReader();
		tiled.core.Map readMap = reader.readMap("resources/puplevel1.tmx");
		Map<Integer, Image> images = new HashMap<Integer, Image>();
		readMap.getTileSets().forEach((tileset) -> {
			tileset.forEach((tile) -> {
				images.put(tile.getId(), fasterImage(tile.getImage()));
				tile.getProperties().list(System.out);
			});
		});
		readMap.getLayers().forEach((layer) -> {
			if (layer instanceof ObjectGroup) {
				System.out.println("Found object group layer");
				ObjectGroup objs = (ObjectGroup) layer;
				objs.forEach((obj) -> {
					System.out.format("%d-%d\n", obj.getX(), obj.getY());
				});
			} else if (layer instanceof TileLayer) {
				System.out.println("Found tile layer group");
				TileLayer tL = (TileLayer) layer;
				int w = tL.getWidth();
				int h = tL.getHeight();
				for (int x = 0; x < w; ++x) {
					for (int y = 0; y < h; ++y) {
						Tile t = tL.getTileAt(x, y);
						if (t != null) {
							Sprite sprite = new Sprite(images.get(t.getId()),
									GamePosition.wrap(x * 6, y * 6, mapViewLayer));
							game.add(sprite);
							// System.out.format("%d-%d-%d\n", t.getId(), x, y);
							if (t.getProperties().size() > 0) {
								t.getProperties().list(System.out);
							}
						}
					}
				}
			}
		});
	}

	private static Sprite													avatar;
	private static GameView												mapViewLayer;
	private static final Map<Integer, KeyHandler>	keyHandlers	= new HashMap<Integer, KeyHandler>();
	static {
		mapViewLayer = GameView.getBuilder(game.getView()).scale(3, 3).build();
		InputStream stream = Pup8Bit.class.getClassLoader()
				.getResourceAsStream("puppy.png");
		Image img;
		try {
			img = fasterImage(ImageIO.read(stream), getConfig());
			avatar = new Sprite(img,
					GamePosition.wrap(0, 0, mapViewLayer.upALayer()));
			double speed = 5;
			PositionUpdater downU = new DirectionUpdater(avatar, 90, speed);
			PositionUpdater upU = new DirectionUpdater(avatar, 270, speed);
			PositionUpdater leftU = new DirectionUpdater(avatar, 180, speed);
			PositionUpdater rightU = new DirectionUpdater(avatar, 0, speed);
			keyHandlers.put(KeyEvent.VK_DOWN, new KeyHandler(downU));
			keyHandlers.put(KeyEvent.VK_UP, new KeyHandler(upU));
			keyHandlers.put(KeyEvent.VK_LEFT, new KeyHandler(leftU));
			keyHandlers.put(KeyEvent.VK_RIGHT, new KeyHandler(rightU));
			keyHandlers.put(KeyEvent.VK_ESCAPE, new KeyHandler((g, dt) -> {
				g.quit();
			}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initInputSystem(Sprite sprite, JFrame jframe) {
		jframe.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (keyHandlers.containsKey(e.getKeyCode())) {
					keyHandlers.get(e.getKeyCode()).keyPressed(game);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (keyHandlers.containsKey(e.getKeyCode())) {
					keyHandlers.get(e.getKeyCode()).keyReleased(game);
				}
			}

		});
	}

	private static Image fasterImage(Image image) {
		return fasterImage(image, getConfig());
	}

	private static Image fasterImage(Image image, GraphicsConfiguration dc) {
		VolatileImage newImage = dc.createCompatibleVolatileImage(
				image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);
		Graphics g = newImage.createGraphics();
		g.clearRect(0, 0, newImage.getWidth(), newImage.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static void render(Graphics2D g) {
		game.render(GameRenderContext.wrap((Graphics2D) g.create()), renderer);
	}

	public static GraphicsConfiguration getConfig() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
	}
}
