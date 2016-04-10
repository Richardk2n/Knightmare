package com.ares.knightmare.util;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.EntityFactory;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.entities.WaterTile;
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

	public Level(MasterRenderer renderer, Loader loader, WaterFrameBuffers fbos) {
		Entity entity = EntityFactory.createEntity(loader, "lamp", "lamp", "textures", false, new Vector3f(0, 0, -25), new Vector3f(0, 0, 0), 1, 1, 0);

		RawModel modelg = OBJLoader.loadObjModel("fern", loader);
		TexturedModel texturedModelg = new TexturedModel(modelg, new ModelTexture(loader.loadTexture("fern", "textures")));
		ModelTexture textureg = texturedModelg.getTexture();
		textureg.setNumberOfRows(2);
		textureg.setShineDamper(0);
		textureg.setReflectivity(0);
		textureg.setHasTransparency(true);
		Entity gras = new Entity(texturedModelg, new Vector3f(50, 10, -50), 0, 0, 0, 1, 3);

		q = EntityFactory.createEntity(loader, "tree", "tree", "textures", true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1, 0, 0);

		// TODO
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("gras", "textures"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("white", "textures"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("Mauer", "textures"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("sand", "textures"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "maps/blend"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmapA");

		renderer.addEntity(entity);
		renderer.addEntity(gras);
		renderer.addEntity(q);
		renderer.addTerrain(terrain);

		GuiTexture gui = new GuiTexture(loader.loadTexture("health", "textures/gui"), new Vector2f(0.75f, 0.85f), new Vector2f(0.25f, 0.25f));
		renderer.addGui(gui);

		Light light = new Light(new Vector3f(0, 100, 0), new Vector3f(1, 1, 1));// TODO
		// Light light1 = new Light(new Vector3f(150, 100, 100), new Vector3f(0,
		// 0, 1));// TODO
		// Light light2 = new Light(new Vector3f(150, 150, 100), new Vector3f(0,
		// 1, 0));// TODO Light list and get 4 nearest for lighting
		Light light3 = new Light(new Vector3f(0, 0, -25), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		Light light4 = new Light(new Vector3f(0, entity.getModel().getRawModel().getHeight() * 2, -25), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		// renderer.addLight(light);
		// renderer.addLight(light1);
		// renderer.addLight(light2);
		renderer.addLight(light3);
		renderer.addLight(light4);
		renderer.addSun(light);

		WaterTile water = new WaterTile(50, -50, 0);
		renderer.addWater(water);

		new Timer(true).scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				q.tick(terrain);
				water.tick();
			}
		}, 0, 10);
	}

	public Entity geEntity() {
		return q;
	}
}
