package com.zealouscoder.ld35.rendering;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.JFrame;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GameConstants;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.InputSystem;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.movement.GameEvent;
import com.zealouscoder.ld35.rendering.swing.GameComponent;

public class SwingRenderer extends GameRenderer implements GameConstants {

	private int							renderCount	= 0;

	private Set<Renderable>	debug				= new HashSet<Renderable>();

	private JFrame					jframe;

	private InputSystem			input;

	@Override
	public void render(GameRenderContext rc, Game game) {
		renderCount = 0;
		GameObjectBound bounds = game.getBounds();
		game.getRenderables().forEach((entry) -> {
			entry.getValue().forEach((r) -> {
				if (bounds.collides(game.getPosition(), r.getBounds(),
						r.getPosition())) {
					r.render(rc.clone(), game, this);
					if (debug.contains(r)) {
						renderDebugView(rc.clone(), r);
					}
				}
				++renderCount;
			});
		});
		rc.drawAsText(0, 0, String.format("Rendering %d", renderCount));
	}

	private void renderDebugView(GameRenderContext rc, Renderable r) {
		rc.toPosition(r);
		rc.drawDebug(r, null);
	}

	@Override
	public void render(GameRenderContext rc, Game game, Sprite sprite) {
		rc.toPosition(sprite);
		rc.drawSprite(sprite);
	}

	@Override
	public void render(GameRenderContext rc, Game game, GenericGameObject go) {
		render(rc, game, go.getSprite());
		if ("avatar".equals(go.getId())) {
			// draw debug collision lines
			Renderable[] neighbors = game.neighbors(go, go.getPosition());
			debug.clear();
			debug.addAll(Arrays.asList(neighbors));
			Color color = Color.red;
			if (game.isValid(go, go.getPosition())) {
				color = Color.green;
			}
			rc.drawDebug(go, color);
		}
	}

	@Override
	public void createWindow(GameRenderContext rc, String title, Game game) {
		GameComponent comp = new GameComponent(game, rc, this);
		game.add((g, dt) -> {
			if (g.every(FPS, comp)) {
				game.addEvent(new GameEvent("repaint", new Properties()));
			}
		});
		jframe = new JFrame("Pup8Bit");
		input = new InputSystem(game);
		input.init(jframe);
		jframe.setLayout(new BorderLayout());
		jframe.add(comp);
		jframe.pack();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.setVisible(true);
	}

}
