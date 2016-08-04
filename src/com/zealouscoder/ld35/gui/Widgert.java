package com.zealouscoder.ld35.gui;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameRenderer;
import com.zealouscoder.ld35.rendering.GameView;
import com.zealouscoder.ld35.rendering.Renderable;

/**
 * Not exactly a widget it's slightly modified it's now a widgert.
 */
public class Widgert implements Renderable {

  private GameView view;
  private Layout layout;

  public Widgert(GameView view, Layout layout) {
    this.view = view;
    this.layout = layout;
  }

  public GameView getView() {
    return view;
  }

  @Override
  public GamePosition getPosition() {
    return layout.whereAmI(this);
  }

  @Override
  public int compareTo(Renderable o) {
    return 0;
  }

  @Override
  public void render(GameRenderContext rc, Game game, GameRenderer renderer) {
    renderer.render(rc, game, this);
  }

  @Override
  public int getLayer() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isRenderable() {
    return false;
  }

  @Override
  public GameObjectBound getBounds() {
    return null;
  }
  
}
