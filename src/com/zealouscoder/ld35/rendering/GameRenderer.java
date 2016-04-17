package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.GenericGameObject;
import com.zealouscoder.ld35.Sprite;

public abstract class GameRenderer {

	public abstract void render(GameRenderContext rc, Game game);

	public abstract void render(GameRenderContext rc, Sprite sprite);

	public abstract void render(GameRenderContext rc, GenericGameObject go);

}
