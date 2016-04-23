package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Terrain;

public class TerrainHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;//TODO
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
			int xE = (int) Math.floor(terrain.getX()/CHUNCK_SIZE);
			int yE = (int) Math.floor(terrain.getHeight()/CHUNCK_SIZE);
			int zE = (int) Math.floor(terrain.getZ()/CHUNCK_SIZE);

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

	public float getHeightOfTerrain(float x, float z) {
		for (Terrain terrain : this.terrains) {
			int xT = (int) Math.floor(terrain.getX()/CHUNCK_SIZE);
			int zT = (int) Math.floor(terrain.getZ()/CHUNCK_SIZE);
			int xE = (int) Math.floor(x/CHUNCK_SIZE);
			int zE = (int) Math.floor(z/CHUNCK_SIZE);
			if(xT==xE && zT == zE){
				return terrain.getHeightOfTerrain(x, z);
			}
		}
		return 0;
	}
}
