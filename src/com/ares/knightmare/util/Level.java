package com.ares.knightmare.util;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.models.ModelTexture;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.rendering.Loader;
import com.ares.knightmare.rendering.MasterRenderer;
import com.ares.knightmare.rendering.OBJLoader;
import com.ares.knightmare.terrain.Terrain;
import com.ares.knightmare.terrain.TerrainTexture;
import com.ares.knightmare.terrain.TerrainTexturePack;

public class Level {

	private Entity q;

	public Level(MasterRenderer renderer, Loader loader) {
		RawModel model = OBJLoader.loadObjModel("lamp", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("lamp")));
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(1);
		texture.setReflectivity(0);
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);

		RawModel modelg = OBJLoader.loadObjModel("fern", loader);
		TexturedModel texturedModelg = new TexturedModel(modelg, new ModelTexture(loader.loadTexture("fern")));
		ModelTexture textureg = texturedModelg.getTexture();
		textureg.setNumberOfRows(2);
		textureg.setShineDamper(0);
		textureg.setReflectivity(0);
		textureg.setHasTransparency(true);
		Entity gras = new Entity(texturedModelg, new Vector3f(0, 0, -50), 0, 0, 0, 1, 3);

		RawModel modelq = OBJLoader.loadObjModel("tree", loader);
		TexturedModel texturedModelq = new TexturedModel(modelq, new ModelTexture(loader.loadTexture("tree")));
		ModelTexture textureq = texturedModelg.getTexture();
		textureq.setShineDamper(0);
		textureq.setReflectivity(0);
		textureq.setHasTransparency(true);
		q = new Entity(texturedModelq, new Vector3f(0, 0, 0), 0, 0, 0, 1);

		// TODO
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("gras"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("white"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("Mauer"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("sand"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

		renderer.addEntity(entity);
		renderer.addEntity(gras);
		renderer.addEntity(q);
		renderer.addTerrain(terrain);
		
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.75f, 0.85f), new Vector2f(0.25f, 0.25f));
		renderer.addGui(gui);
		
//		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));// TODO
//		Light light1 = new Light(new Vector3f(150, 100, 100), new Vector3f(0, 0, 1));// TODO
//		Light light2 = new Light(new Vector3f(150, 150, 100), new Vector3f(0, 1, 0));// TODO
		Light light3 = new Light(new Vector3f(0, 0, -25), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		Light light4 = new Light(new Vector3f(0, entity.getModel().getRawModel().getHeight()*2, -25), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
//		renderer.addLight(light);
//		renderer.addLight(light1);
//		renderer.addLight(light2);
		renderer.addLight(light3);
		renderer.addLight(light4);

		new Timer(true).scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				q.tick(terrain);
			}
		}, 0, 10);
	}

	public Entity geEntity() {
		return q;
	}
}
