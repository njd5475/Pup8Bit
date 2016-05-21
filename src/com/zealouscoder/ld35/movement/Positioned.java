package com.zealouscoder.ld35.movement;

import com.zealouscoder.ld35.rendering.Renderable;

public interface Positioned extends Comparable<Renderable> {

	public GamePosition getPosition();
	
}
