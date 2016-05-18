package com.zealouscoder.ld35.partitioning;

import java.util.HashSet;
import java.util.Set;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public class QuadTreePartitionStrategy implements SpacePartitionStrategy {

	private QuadTreePartitionStrategy[]	children	= new QuadTreePartitionStrategy[4];
	private Set<Renderable>							objects		= new HashSet<Renderable>();
	private GamePosition								position;
	private GameObjectBound							bounds;

	public QuadTreePartitionStrategy(GamePosition position,
			GameObjectBound bound) {

	}

	@Override
	public void add(Renderable r) {
		// TODO Auto-generated method stub

	}

	@Override
	public Renderable[] get(Renderable r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Renderable[] get(GamePosition pos, GameObjectBound bound) {
		// TODO Auto-generated method stub
		return null;
	}

}
