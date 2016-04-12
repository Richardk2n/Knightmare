package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.WaterTile;

public class WaterHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private List<WaterTile> waters = new ArrayList<>();

	public void store(WaterTile water) {
		waters.add(water);
	}

	public void remove(WaterTile water) {
		waters.remove(water);
	}

	public List<WaterTile> getRenderedWaters(Camera camera) {
		Vector3f position = camera.getPosition(); // TODO view direction
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);

		List<WaterTile> waters = new ArrayList<>();
		for (WaterTile water : this.waters) {
			int xE = (int) Math.floor(water.getX()/CHUNCK_SIZE);
			int yE = (int) Math.floor(water.getHeight()/CHUNCK_SIZE);
			int zE = (int) Math.floor(water.getZ()/CHUNCK_SIZE);

			if (x - RENDER_DISTANCE <= xE && xE <= x + RENDER_DISTANCE && y - RENDER_DISTANCE <= yE && yE <= y + RENDER_DISTANCE && z - RENDER_DISTANCE <= zE
					&& zE <= z + RENDER_DISTANCE) {
				waters.add(water);
			}
		}
		return waters;
	}

	public void tick() {
		for (WaterTile water : waters) {
			// water.tick();//TODO
		}
	}
}
