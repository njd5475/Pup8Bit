package com.zealouscoder.ld35.partitioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameObjectBound;
import com.zealouscoder.ld35.rendering.Renderable;

public class IntegerBinPartitionStrategy implements SpacePartitionStrategy {

	// just make the number of possible bins really large
	private static final int							BIN_OFFSET	= Integer.MAX_VALUE / 4;
	private static final int							BIN_COLUMNS	= (int) Math
			.sqrt(BIN_OFFSET);

	private int														binSize			= -1;
	private int														defaultSizeMultiplier = 2;

	private Map<Integer, Set<Renderable>>	partitions	= new HashMap<Integer, Set<Renderable>>();

	public IntegerBinPartitionStrategy() {
	}

	public IntegerBinPartitionStrategy(int binSize) {
		this.binSize = binSize;
	}

	public void add(Renderable r) {
		if (r != null && r.isRenderable()) {
			if (binSize == -1) {
				binSize = (int) (r.getBounds().getWidth() * defaultSizeMultiplier);
			}
			Integer[] bins = getBins(r.getPosition(), r.getBounds());
			for (int bin : bins) {
				Set<Renderable> set = partitions.get(bin);
				if (set == null) {
					set = new HashSet<Renderable>();
					partitions.put(bin, set);
				}
				set.add(r);
			}
		}
	}

	public Renderable[] get(Renderable r) {
		return get(r.getPosition(), r.getBounds());
	}

	public Renderable[] get(GamePosition pos, GameObjectBound bound) {
		Integer[] bins = getBins(pos, bound);
		Set<Renderable> found = new HashSet<Renderable>();
		for (int bin : bins) {
			Set<Renderable> set = partitions.get(bin);
			if (set != null) {
				found.addAll(set);
			}
		}
		return found.toArray(new Renderable[found.size()]);
	}

	private Integer[] getBins(GamePosition pos, GameObjectBound bound) {
		Set<Integer> bins = new HashSet<Integer>();
		int top = (int) (pos.getY() / binSize);
		int bottom = (int) ((pos.getY() + bound.getHeight()) / binSize);
		int left = (int) (pos.getX() / binSize);
		int right = (int) ((pos.getX() + bound.getWidth()) / binSize);

		for (int x = left; x <= right; ++x) {
			for (int y = top; y <= bottom; ++y) {
				bins.add(getBin(x, y));
			}
		}

		return bins.toArray(new Integer[bins.size()]);
	}

	private int getBin(int x, int y) {
		return BIN_OFFSET + (y * BIN_COLUMNS + x);
	}
}
