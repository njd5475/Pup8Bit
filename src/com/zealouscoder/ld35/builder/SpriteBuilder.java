package com.zealouscoder.ld35.builder;

import com.zealouscoder.ld35.Game;
import com.zealouscoder.ld35.ImageResource;
import com.zealouscoder.ld35.Sprite;
import com.zealouscoder.ld35.movement.GamePosition;

public class SpriteBuilder {

	private static Sprite	placeholder;
	private Game					game;

	public SpriteBuilder(Game game) {
		this.game = game;
	}

	public static Sprite buildPlaceholder() {
		if (placeholder == null) {
			placeholder = new Sprite(ImageResource.PLACEHOLDER, 32, 32, GamePosition.ANCHOR);
		}
		return placeholder;
	}

}
