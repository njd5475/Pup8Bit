package com.zealouscoder.ld35.rendering;

public class MapGridView extends GameView {

	private int tileWidth;
	private int tileHeight;

	private MapGridView(int tileImageWidth, int tileImageHeight) {
		super(0,0,1,1,0,0,0,0);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

}
