package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.Sprite;

public class SwingRenderer extends GameRenderer {

	private int renderCount = 0;

	@Override
	public void render(GameRenderContext rc, Game game) {
		renderCount = 0;
		game.getRenderables().forEach((entry) -> {
			entry.getValue().forEach((r) -> {
				r.render(rc.clone(), this);
				++renderCount;
			});
		});
		rc.drawAsText(0, 0, String.format("Rendering %d", renderCount));
	}

	@Override
	public void render(GameRenderContext rc, Sprite sprite) {
		rc.toPosition(sprite);
		rc.drawSprite(sprite);
	}

}
