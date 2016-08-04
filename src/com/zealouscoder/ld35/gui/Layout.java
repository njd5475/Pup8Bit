package com.zealouscoder.ld35.gui;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameView;

public class Layout {

  private GameView view;

  public Layout(GameView view) {
    this.view = view;
  }

  public GamePosition whereAmI(Widgert widgert) {
    return GamePosition.ANCHOR;
  }

}
