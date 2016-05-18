package com.zealouscoder.ld35;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public class SpacePartitionStrategy {

	private Map<Integer, Set<Renderable>> bins = new HashMap<Integer, Set<Renderable>>();
	
	public void add(Renderable r) {
		
	}
	
	public Renderable[] get(GameObjectBound bounds) {
		return null;
	}
	
	private int[] getBins(Renderable r) {
		return new int[]{};
	}
}
