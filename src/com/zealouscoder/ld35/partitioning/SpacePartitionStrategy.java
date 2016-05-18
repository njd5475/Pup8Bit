package com.zealouscoder.ld35.partitioning;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public class SpacePartitionStrategy {

	private double binSize = 0;
	
	private Map<Integer, Set<Renderable>> bins = new HashMap<Integer, Set<Renderable>>();
	
	public SpacePartitionStrategy(double binSize) {
		this.binSize = binSize;
		
	}
	
	public void add(Renderable r) {
	}
	
	public Renderable[] get(GamePosition pos, GameObjectBound bound) {
		
		return null;
	}
	
	private int[] getBins(Renderable r) {
		int topleft = 0;
		int topright = 0;
		int bottomleft = 0;
		int bottomright = 0;
		
		return new int[]{};
	}
	
}
