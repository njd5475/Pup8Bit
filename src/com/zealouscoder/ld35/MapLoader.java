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

	private String						mapFile;
	private GameRenderContext	context;
	private Game							game;

	public MapLoader(String map, GameRenderContext context, Game game) {
		this.mapFile = map;
		this.context = context;
		this.game = game;
	}

	private void loadMaps(String mapFile) throws Exception {
		TMXMapReader reader = new TMXMapReader();
		tiled.core.Map readMap = reader.readMap(mapFile);
		Map<Integer, ImageResource> images = new HashMap<Integer, ImageResource>();
		readMap.getTileSets().forEach((tileset) -> {
			tileset.forEach((tile) -> {
				if (!images.containsKey(tile.getId())) {
					images.put(tile.getId(), context
							.loadImageResource("tile" + tile.getId(), tile.getImage()));
				}
			});
		});

		int layerCount = 0;
		readMap.getLayers().forEach((layer) -> {
			if (layer instanceof ObjectGroup) {
				System.out.println("Found object group layer");
				ObjectGroup objs = (ObjectGroup) layer;
				objs.forEach((obj) -> {
					Properties objProps = obj.getProperties();
					objProps.put("SpawnPosition", GamePosition.wrap(obj.getX(),
							obj.getY(), game.getMapViewLayer()));
					GenericGameObject go = new GenericGameObject(
							"obj" + obj.getX() + ":" + obj.getY(), obj.getType(), null,
							objProps);
					game.add(go);
					System.out.println(obj.getType());
				});
			} else if (layer instanceof TileLayer) {
				System.out.println("Found tile layer group");
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
							System.out.format("Tile at %d,%d with size %d,%d\n", x, y,
									t.getWidth(), t.getHeight());
							GenericGameObject go = new GenericGameObject("tile" + x + ":" + y,
									"tile" + t.getId(), sprite, t.getProperties());
							game.add(go);
						}
					}
				}
			}
		});
	}

	public void load() {
		try {
			loadMaps(mapFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
