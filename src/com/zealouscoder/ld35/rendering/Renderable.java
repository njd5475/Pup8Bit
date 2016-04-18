package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.movement.Positioned;

public interface Renderable extends Positioned {

	public void render(GameRenderContext rc, Game game, GameRenderer renderer);

	public GameView getView();
	
	public int getLayer();

	public boolean isRenderable();

	public GameObjectBound getBounds();
	
}
