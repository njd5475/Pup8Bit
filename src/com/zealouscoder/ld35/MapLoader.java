package com.zealouscoder.ld35;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.zealouscoder.ld35.movement.GamePosition;
import com.zealouscoder.ld35.rendering.GameRenderContext;
import com.zealouscoder.ld35.rendering.GameView;

import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

public class MapLoader {

	private Game game;

	public MapLoader(Game game) {
		this.game = game;
	}

	public void loadMap(String mapFile) throws Exception {
		TMXMapReader reader = new TMXMapReader();
		tiled.core.Map readMap = reader.readMap(mapFile);
		Map<Integer, ImageResource> images = new HashMap<Integer, ImageResource>();
		readMap.getTileSets().forEach((tileset) -> {
			tileset.forEach((tile) -> {
				if (!images.containsKey(tile.getId())) {
					images.put(tile.getId(),
							game.loadImageResource("tile" + tile.getId(), tile.getImage()));
				}
			});
		});

		int layerCount = 0;
		readMap.getLayers().forEach((layer) -> {
			if (layer instanceof ObjectGroup) {
				ObjectGroup objs = (ObjectGroup) layer;
				objs.forEach((obj) -> {
					Properties objProps = obj.getProperties();
					objProps.put("SpawnPosition", GamePosition.wrap(obj.getX(),
							obj.getY(), game.getMapViewLayer()));
					GenericGameObject go = new GenericGameObject(
							"obj" + obj.getX() + ":" + obj.getY(), obj.getType(), null,
							objProps);
					game.add(go);
				});
			} else if (layer instanceof TileLayer) {
				TileLayer tL = (TileLayer) layer;
				int w = tL.getWidth();
				int h = tL.getHeight();
				String layerStr = tL.getProperties().getProperty("layer");
				int l = layerCount;
				try {
					l = Integer.parseInt(layerStr);
				} catch (NumberFormatException nfe) {
				}
				GameView layerView = game.getMapViewLayer().forLayer(l);
				for (int x = 0; x < w; ++x) {
					for (int y = 0; y < h; ++y) {
						Tile t = tL.getTileAt(x, y);
						if (t != null) {
							Sprite sprite = new Sprite(images.get(t.getId()), t.getWidth(),
									t.getHeight(), GamePosition.wrap(x * t.getWidth(),
											y * t.getHeight(), layerView));
							GenericGameObject go = new GenericGameObject("tile" + x + ":" + y,
									"tile" + t.getId(), sprite, t.getProperties());
							game.add(go);
						}
					}
				}
			}
		});
	}
}
