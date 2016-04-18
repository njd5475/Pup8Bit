package com.zealouscoder.ld35.rendering;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.Sprite;

public class SwingRenderer extends GameRenderer {

	private int renderCount = 0;

	private Set<Renderable> debug = new HashSet<Renderable>();
	
	@Override
	public void render(GameRenderContext rc, Game game) {
		renderCount = 0;
		game.getRenderables().forEach((entry) -> {
			entry.getValue().forEach((r) -> {
				r.render(rc.clone(), game, this);
				if(debug.contains(r)) {
					renderDebugView(rc.clone(), r);
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
			if(game.isValid(go, go.getPosition())) {
				color = Color.green;
			}
			rc.drawDebug(go, color);
		}
	}

}
