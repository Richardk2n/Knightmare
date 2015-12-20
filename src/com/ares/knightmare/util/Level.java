package com.ares.knightmare.util;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.rendering.Loader;
import com.ares.knightmare.rendering.MasterRenderer;
import com.ares.knightmare.rendering.OBJLoader;
import com.ares.knightmare.terrain.Terrain;
import com.ares.knightmare.textures.ModelTexture;
import com.ares.knightmare.textures.TerrainTexture;
import com.ares.knightmare.textures.TerrainTexturePack;

public class Level {
	
	public Level(MasterRenderer renderer, Loader loader){
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(1);
		texture.setReflectivity(0);
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 160, 0, 1);

		RawModel modelg = OBJLoader.loadObjModel("fern", loader);
		TexturedModel texturedModelg = new TexturedModel(modelg, new ModelTexture(loader.loadTexture("fern")));
		ModelTexture textureg = texturedModelg.getTexture();
		textureg.setShineDamper(0);
		textureg.setReflectivity(0);
		textureg.setHasTransparency(true);
		Entity gras = new Entity(texturedModelg, new Vector3f(0, 0, -50), 0, 160, 0, 1);
		
		//TODO
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("gras"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("white"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("Mauer"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("sand"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		
		renderer.addEntity(entity);
		renderer.addEntity(gras);
		renderer.addTerrain(terrain);
	}
}
