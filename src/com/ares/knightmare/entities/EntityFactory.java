package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.loader.ObjLoader;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.textures.ModelTexture;

public class EntityFactory {

	public static Entity createEntity(Loader loader, String modelName, String textureName, String textureType, boolean hasTransparency, Vector3f position,
			Vector3f rotation, int scale, int shineDampener, int reflectivity) {
		RawModel texturedModel = ObjLoader.loadOBJ(modelName, loader, false);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureName, textureType));
		texture.setShineDamper(shineDampener);
		texture.setReflectivity(reflectivity);
		texture.setHasTransparency(hasTransparency);
		texturedModel.setTexture(texture);
		return new Entity(texturedModel, position, rotation.x, rotation.y, rotation.z, scale);
	}

	public static Entity createEntity(Loader loader, String modelName, String textureName, String textureType, boolean hasTransparency, Vector3f position,
			Vector3f rotation, int scale, int shineDampener, int reflectivity, int rowCount, int textureId) {
		RawModel texturedModel = ObjLoader.loadOBJ(modelName, loader, false);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureName, textureType));
		texture.setNumberOfRows(rowCount);
		texture.setShineDamper(shineDampener);
		texture.setReflectivity(reflectivity);
		texture.setHasTransparency(hasTransparency);
		texturedModel.setTexture(texture);
		return new Entity(texturedModel, position, rotation.x, rotation.y, rotation.z, scale, textureId);
	}
}
