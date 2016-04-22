package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.loader.OBJLoader;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.textures.ModelTexture;

public class EntityFactory {

	public static Entity createEntity(Loader loader, String modelName, String textureName, String textureType, boolean hasTransparency, Vector3f position,
			Vector3f rotation, int scale, int shineDampener, int reflectivity) {
		TexturedModel texturedModel = new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(loader.loadTexture(textureName, textureType)));
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(shineDampener);
		texture.setReflectivity(reflectivity);
		texture.setHasTransparency(hasTransparency);
		return new Entity(texturedModel, position, rotation.x, rotation.y, rotation.z, scale);
	}

	public static Entity createEntity(Loader loader, String modelName, String textureName, String textureType, boolean hasTransparency, Vector3f position,
			Vector3f rotation, int scale, int shineDampener, int reflectivity, int rowCount, int textureId) {
		TexturedModel texturedModel = new TexturedModel(OBJLoader.loadObjModel(modelName, loader), new ModelTexture(loader.loadTexture(textureName, textureType)));
		ModelTexture texture = texturedModel.getTexture();
		texture.setNumberOfRows(rowCount);
		texture.setShineDamper(shineDampener);
		texture.setReflectivity(reflectivity);
		texture.setHasTransparency(hasTransparency);
		return new Entity(texturedModel, position, rotation.x, rotation.y, rotation.z, scale, textureId);
	}
}
