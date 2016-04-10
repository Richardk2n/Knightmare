package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.terrain.Terrain;

public class TerrainHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private List<Terrain> terrains = new ArrayList<>();

	public void store(Terrain Terrain) {
		terrains.add(Terrain);
	}

	public void remove(Terrain Terrain) {
		terrains.remove(Terrain);
	}

	public List<Terrain> getRenderedTerrains(Camera camera) {
		Vector3f position = camera.getPosition(); // TODO view direction
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);

		List<Terrain> terrains = new ArrayList<>();
		for (Terrain terrain : this.terrains) {
			int xE = (int) terrain.getX();
			int yE = 0;
			int zE = (int) terrain.getZ();

			if (x - RENDER_DISTANCE <= xE && xE <= x + RENDER_DISTANCE && y - RENDER_DISTANCE <= yE && yE <= y + RENDER_DISTANCE && z - RENDER_DISTANCE <= zE
					&& zE <= z + RENDER_DISTANCE) {
				terrains.add(terrain);
			}
		}
		return terrains;
	}

	public void tick() {
		for (Terrain terrain : terrains) {
			// terrain.tick();//TODO
		}
	}
}
