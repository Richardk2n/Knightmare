package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;

public class ChunkHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Entity>>>> entities = new HashMap<>();
	private List<Entity> entitiesList = new ArrayList<>();

	public void store(Entity entity) {
		Vector3f position = entity.getPosition();
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);
		
		HashMap<Integer, HashMap<Integer, List<Entity>>> xMap = entities.get(x);
		if(xMap == null){
			xMap = new HashMap<>();
			entities.put(x, xMap);
		}
		HashMap<Integer, List<Entity>> yMap = xMap.get(y);
		if(yMap == null){
			yMap = new HashMap<>();
			xMap.put(y, yMap);
		}
		List<Entity> zList = yMap.get(z);
		if(zList == null){
			zList = new ArrayList<>();
			yMap.put(z, zList);
		}
		zList.add(entity);
		entitiesList.add(entity);
	}
	
	public void remove(Entity entity){
		Vector3f position = entity.getPosition();
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);
		
		HashMap<Integer, HashMap<Integer, List<Entity>>> xMap = entities.get(x);
		HashMap<Integer, List<Entity>> yMap = xMap.get(y);
		List<Entity> zList = yMap.get(z);
		zList.remove(entity);
		entitiesList.remove(entity);
		
		if(zList.isEmpty()){
			yMap.remove(zList);
			if(yMap.isEmpty()){
				xMap.remove(yMap);
				if(xMap.isEmpty()){
					entities.remove(xMap);
				}
			}
		}
	}
	
	public List<Entity> getRenderedEntities(Camera camera){
		Vector3f position = camera.getPosition(); //TODO view direction
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);
		
		List<Entity> entities = new ArrayList<>();
		for(int a = x-RENDER_DISTANCE; a<=x+RENDER_DISTANCE; a++){
			for(int b = y-RENDER_DISTANCE; b<=y+RENDER_DISTANCE; b++){
				for(int c = z-RENDER_DISTANCE; c<=z+RENDER_DISTANCE; c++){
					entities.addAll(getEntitiesIn(a, b, c));
				}
			}
		}
		return entities;
	}
	
	private List<Entity> getEntitiesIn(int x, int y, int z){
		HashMap<Integer, HashMap<Integer, List<Entity>>> xMap = entities.get(x);
		if(xMap != null){
			HashMap<Integer, List<Entity>> yMap = xMap.get(y);
			if(yMap != null){
				List<Entity> zList = yMap.get(z);
				if(zList != null){
					return zList;
				}
			}
		}
		return new ArrayList<>();
	}
	
	public void tick(){
		for(Entity entity: entitiesList){
			entity.tick();
		}
	}
}
