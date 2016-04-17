package com.zealouscoder.ld35;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.SwingRenderer;
import com.zealouscoder.ld35.rendering.swing.GameComponent;

public class Pup8Bit implements GameConstants {

	private static Sprite								avatar;
	private static final Game						game			= new Game(NAME);
	private static GameRenderContext		context		= GameRenderContext.wrap(null);
	private static final MapLoader			mapLoader	= new MapLoader(
			"resources/puplevel1.tmx", context, game);
	private static final SwingRenderer	renderer	= new SwingRenderer();

	static {
		System.setProperty("sun.java2d.opengl", "True");

		InputStream stream = Pup8Bit.class.getClassLoader()
				.getResourceAsStream("puppy.png");
		BufferedImage img;
		try {
			img = ImageIO.read(stream);
			avatar = new Sprite(context.loadImageResource("avatar", img),
					img.getWidth(), img.getHeight(),
					GamePosition.wrap(0, 0, game.getMapViewLayer().forLayer(20)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final InputSystem		input			= new InputSystem(avatar, game);
	
	public static void main(String[] args) throws IOException {
		game.start();
		GameComponent comp = new GameComponent(game, context, renderer);
		JFrame jframe = createWindow(comp);
		mapLoader.load();
		game.add((g, dt) -> {
			if (g.every(FPS, comp)) {
				comp.repaint();
			}
		});
		game.add(avatar);
		input.init(jframe);
		jframe.setVisible(true);
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
