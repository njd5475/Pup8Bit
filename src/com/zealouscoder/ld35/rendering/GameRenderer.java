package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.gui.Widgert;

public abstract class GameRenderer {

	public abstract void createWindow(GameRenderContext rc, String title, Game game);
	
	public abstract void render(GameRenderContext rc, Game game);

	public abstract void render(GameRenderContext rc, Game game, Sprite sprite);

	public abstract void render(GameRenderContext rc, Game game,
			GenericGameObject go);

  public abstract void render(GameRenderContext rc, Game game, Widgert widgert);

}
