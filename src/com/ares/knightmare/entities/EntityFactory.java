package com.ares.knightmare.entities;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.loader.ObjLoader;
import com.ares.knightmare.models.Model;
import com.ares.knightmare.textures.ModelTexture;

public class EntityFactory {

	private static HashMap<String, Model> models = new HashMap<>();

	public static void registerModel(Loader loader, String name, boolean hasTransparency, float shineDampener, float reflectivity, int rowCount) {
		boolean isNormalMapped = Class.class
				.getResourceAsStream(new StringBuilder("/").append("maps/normal").append("/").append(name).append("Normal.png").toString()) != null;
		if (models.get(name) == null) {
			Model model = ObjLoader.loadOBJ(name, loader, isNormalMapped);
			ModelTexture texture = new ModelTexture(loader.loadTexture(name, "textures"));
			texture.setNumberOfRows(rowCount);
			texture.setShineDamper(shineDampener);
			texture.setReflectivity(reflectivity);
			texture.setHasTransparency(hasTransparency);
			if (isNormalMapped) {
				texture.setNormalMap(loader.loadTexture(new StringBuilder(name).append("Normal").toString(), "maps/normal"));
			}
			model.setTexture(texture);
			models.put(name, model);
		}
	}
	
	public static void registerModel(Loader loader, String name, boolean hasTransparency, float shineDampener, float reflectivity) {
		boolean isNormalMapped = Class.class
				.getResourceAsStream(new StringBuilder("/").append("maps/normal").append("/").append(name).append("Normal.png").toString()) != null;
		if (models.get(name) == null) {
			Model model = ObjLoader.loadOBJ(name, loader, isNormalMapped);
			ModelTexture texture = new ModelTexture(loader.loadTexture(name, "textures"));
			texture.setShineDamper(shineDampener);
			texture.setReflectivity(reflectivity);
			texture.setHasTransparency(hasTransparency);
			if (isNormalMapped) {
				texture.setNormalMap(loader.loadTexture(new StringBuilder(name).append("Normal").toString(), "maps/normal"));
			}
			model.setTexture(texture);
			models.put(name, model);
		}
	}

	public static Entity createEntity(String name, Vector3f position, Vector3f rotation, float scale, int textureId) {
		Model model = models.get(name);
		if (model == null) {
			return null;
		}
		return new Entity(model, position, rotation.x, rotation.y, rotation.z, scale, textureId);
	}
	
	public static Entity createEntity(String name, Vector3f position, Vector3f rotation, float scale) {
		Model model = models.get(name);
		if (model == null) {
			return null;
		}
		return new Entity(model, position, rotation.x, rotation.y, rotation.z, scale);
	}
	


	public static Entity createEntity(String name, Vector3f position, float scale, int textureId) {
		Model model = models.get(name);
		if (model == null) {
			return null;
		}
		return new Entity(model, position, 0, 0, 0, scale, textureId);
	}
	
	public static Entity createEntity(String name, Vector3f position, float scale) {
		Model model = models.get(name);
		if (model == null) {
			return null;
		}
		return new Entity(model, position, 0, 0, 0, scale);
	}
}
