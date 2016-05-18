package com.zealouscoder.ld35.partitioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public class IntegerBinPartitionStrategy implements SpacePartitionStrategy {

	private static final int BIN_OFFSET = Integer.MAX_VALUE/4;
	private int binSize = -1;
	
	private Map<Integer, Set<Renderable>> partitions = new HashMap<Integer, Set<Renderable>>();
	
	public IntegerBinPartitionStrategy() {
	}
	
	public IntegerBinPartitionStrategy(int binSize) {
		this.binSize = binSize;
	}
	
	public void add(Renderable r) {
		if(binSize == -1) {
			binSize = (int) (r.getBounds().getWidth()*4);
		}
		int[] bins = getBins(r.getPosition(), r.getBounds());
		for(int bin : bins) {
			Set<Renderable> set = partitions.get(bin);
			if(set == null) {
				set = new HashSet<Renderable>();
				partitions.put(bin, set);
			}
			set.add(r);
		}
	}
	
	public Renderable[] get(Renderable r) {
		return get(r.getPosition(), r.getBounds());
	}
	
	public Renderable[] get(GamePosition pos, GameObjectBound bound) {
		int bins[] = getBins(pos, bound);
		Set<Renderable> found = new HashSet<Renderable>();
		for(int bin : bins) {
			found.addAll(partitions.get(bin));
		}
		return found.toArray(new Renderable[found.size()]);
	}
	
	private int[] getBins(GamePosition pos, GameObjectBound bound) {
		Set<Integer> bins = new HashSet<Integer>();
		bins.add(getBin(pos.getX(), pos.getY()));
		bins.add(getBin(pos.getX()+bound.getWidth(), pos.getY()));
		int bottomleft = 0;
		int bottomright = 0;
		
		
		
		return new int[]{};
	}
	
	private int getBin(double x, double y) {
		return BIN_OFFSET + ((int)(y/binSize) * binSize + ((int)x/binSize));
	}
}
