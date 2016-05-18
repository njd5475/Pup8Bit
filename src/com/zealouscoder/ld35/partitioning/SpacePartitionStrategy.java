package com.zealouscoder.ld35.partitioning;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public interface SpacePartitionStrategy {

	public void add(Renderable r);

	public Renderable[] get(Renderable r);

	public Renderable[] get(GamePosition pos, GameObjectBound bound);

}
