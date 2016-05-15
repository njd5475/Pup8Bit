package com.zealouscoder.ld35.rendering;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zealouscoder.ld35.movement.GamePosition;

public class GameObjectBoundTest {

	private GameObjectBound bounds = new GameObjectBound(2.25, 1.33);
	
	@Test
	public void testCollides() {
		GameObjectBound otherBounds = new GameObjectBound(5,5);
		GamePosition thisPos = GamePosition.wrap(0, 0, GameView.ANCHOR);
		GamePosition other = GamePosition.wrap(0, 0, GameView.ANCHOR);
		assertTrue(bounds.collides(thisPos, otherBounds, other));
		other = GamePosition.wrap(2.25, 1.33, GameView.ANCHOR);
		assertFalse(bounds.collides(thisPos, otherBounds, other));
		other = GamePosition.wrap(2.24, 1.32, GameView.ANCHOR);
		assertTrue(bounds.collides(thisPos, otherBounds, other));
	}

	@Test
	public void testInside() {
		assertTrue(bounds.inside(GamePosition.wrap(.1, .1, GameView.ANCHOR), 1, 1));
		assertFalse(bounds.inside(GamePosition.wrap(0, 0, GameView.ANCHOR), 2.25, 1.33));
		assertFalse(bounds.inside(GamePosition.wrap(1, 1, GameView.ANCHOR), 1, 1));
	}

}
