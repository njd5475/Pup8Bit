package com.zealouscoder.ld35.rendering;

public interface Renderable extends Positioned {

	public void render(GameRenderContext rc, GameRenderer renderer);

	public GameView getView();
	
	public int getLayer();
	
}
