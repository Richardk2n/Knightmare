package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.models.Model;

public class NormalMappedEntityHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private List<Entity> entities = new ArrayList<>();
	private Map<Model, List<Entity>> entitiesModel = new HashMap<>();

	public void store(Entity entity) {
		entities.add(entity);
		Model entityModel = entity.getModel();
		List<Entity> batch = entitiesModel.get(entityModel);
		if (batch == null) {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entitiesModel.put(entityModel, newBatch);
		} else {
			batch.add(entity);
		}
	}

	public void remove(Entity entity) {
		entities.remove(entity);
		Model entityModel = entity.getModel();
		List<Entity> list = entitiesModel.get(entityModel);
		list.remove(entity);
		if (list.isEmpty()) {
			entitiesModel.remove(list);
		}
	}

	public Map<Model, List<Entity>> getRenderedEntities(Camera camera) {
		Vector3f position = camera.getPosition(); // TODO view direction
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);

		List<Entity> entities = new ArrayList<>();
		for (Entity entity : this.entities) {
			Vector3f positionE = entity.getPosition();
			int xE = (int) Math.floor(positionE.x / CHUNCK_SIZE);
			int yE = (int) Math.floor(positionE.y / CHUNCK_SIZE);
			int zE = (int) Math.floor(positionE.z / CHUNCK_SIZE);

			if (x - RENDER_DISTANCE <= xE && xE <= x + RENDER_DISTANCE && y - RENDER_DISTANCE <= yE && yE <= y + RENDER_DISTANCE && z - RENDER_DISTANCE <= zE
					&& zE <= z + RENDER_DISTANCE) {
				entities.add(entity);
			}
		}
		return sort(entities);
	}

	private Map<Model, List<Entity>> sort(List<Entity> entities) {
		Map<Model, List<Entity>> map = new HashMap<>();

		for (Entity entity : entities) {
			Model entityModel = entity.getModel();
			List<Entity> batch = map.get(entityModel);
			if (batch == null) {
				List<Entity> newBatch = new ArrayList<>();
				newBatch.add(entity);
				map.put(entityModel, newBatch);
			} else {
				batch.add(entity);
			}
		}
		return map;
	}

	public void tick() {
		for (List<Entity> list : entitiesModel.values()) {
			for (Entity entity : list) {
//				 entity.tick();//TODO
			}
		}
	}
}
