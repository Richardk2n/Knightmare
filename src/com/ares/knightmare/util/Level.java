package com.ares.knightmare.util;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.EntityFactory;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.entities.ParticleSystem;
import com.ares.knightmare.entities.Terrain;
import com.ares.knightmare.entities.WaterTile;
import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.rendering.MasterRenderer;
import com.ares.knightmare.textures.GuiTexture;
import com.ares.knightmare.textures.ParticleTexture;
import com.ares.knightmare.textures.TerrainTexturePack;
import com.ares.knightmare.util.fontMeshCreator.FontType;
import com.ares.knightmare.util.fontMeshCreator.GUIText;

public class Level {

	private Entity q;
	private MasterRenderer renderer;

	public Level(MasterRenderer renderer, Loader loader) {
		this.renderer = renderer;
		EntityFactory.registerModel(loader, "lamp", false, 1, 0);
		Entity entity = EntityFactory.createEntity("lamp", new Vector3f(35, 0, -35), 1);

		EntityFactory.registerModel(loader, "fern", true, 0, 0, 2);
		Entity gras = EntityFactory.createEntity("fern", new Vector3f(25, 0, -25), 1, 1);

		EntityFactory.registerModel(loader, "tree", false, 0, 0);
		q = EntityFactory.createEntity("tree", new Vector3f(0, 0, 0), 2);
		
		Entity fern2 = EntityFactory.createEntity("fern", new Vector3f(-10, 15, 10), 1, 3);

		// TODO
		int backgroundTexture = loader.loadTexture("gras", "textures");
		int rTexture = loader.loadTexture("white", "textures");
		int gTexture = loader.loadTexture("Mauer", "textures");
		int bTexture = loader.loadTexture("sand", "textures");

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
		int blendMap = loader.loadTexture("blendMap", "maps/blend");
		Terrain terrain = new Terrain(0, -1, 0, loader, texturePack, blendMap, "heightmapA");
//		Terrain terrain = new Terrain(0, -1, 0, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(0, -2, 10, loader, texturePack, blendMap, "heightmapB");
		Terrain terrain3 = new Terrain(-1, -1, 10, loader, texturePack, blendMap, "heightmapB");
		Terrain terrain4 = new Terrain(-1, -2, 10, loader, texturePack, blendMap, "heightmapB");
		Terrain terrain5 = new Terrain(0, 0, 10, loader, texturePack, blendMap, "heightmapB");
		Terrain terrain6 = new Terrain(-1, 0, 10, loader, texturePack, blendMap, "heightmapB");

		renderer.addEntity(entity);
		renderer.addEntity(gras);
		renderer.addEntity(fern2);
		renderer.addEntity(q);
		renderer.addTerrain(terrain);
		renderer.addTerrain(terrain2);
		renderer.addTerrain(terrain3);
		renderer.addTerrain(terrain4);
		renderer.addTerrain(terrain5);
		renderer.addTerrain(terrain6);

		GuiTexture gui = new GuiTexture(loader.loadTexture("health", "textures/gui"), new Vector2f(0.75f, 0.85f), new Vector2f(0.25f, 0.25f));
		renderer.addGui(gui);
		
//		GuiTexture gui2 = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
//		renderer.addGui(gui2);

		Light light = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));// TODO
		// Light light1 = new Light(new Vector3f(150, 100, 100), new Vector3f(0,
		// 0, 1));// TODO
		// Light light2 = new Light(new Vector3f(150, 150, 100), new Vector3f(0,
		// 1, 0));// TODO Light list and get 4 nearest for lighting
		Light light3 = new Light(new Vector3f(35, 0, -35), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		Light light4 = new Light(new Vector3f(35, entity.getModel().getHeight() * 2 - 10, -35), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		// renderer.addLight(light);
		// renderer.addLight(light1);
//		 renderer.addLight(light2);
		renderer.addLight(light3);
		renderer.addLight(light4);
		renderer.addSun(light);

		WaterTile water = new WaterTile(50, -50, 0);
		renderer.addWater(water);
		
		EntityFactory.registerModel(loader, "barrel", false, 5, 0.25f);
		renderer.addNormalMappedEntity(EntityFactory.createEntity("barrel", new Vector3f(0, 8, 40), 0.25f));
		
		EntityFactory.registerModel(loader, "foot", false, 5, 0);
		renderer.addNormalMappedEntity(EntityFactory.createEntity("foot", new Vector3f(0, 8, -20), 0.25f));
		
		FontType font = new FontType(loader.loadTexture("candara", "textures/font"), "/textures/font/candara.fnt");
		GUIText text = new GUIText("Hello, you fool! Have a nice day.", 3, font, new Vector2f(0.125f, 0.125f), 0.75f, true);
		text.setColour(1, 0, 0);
		renderer.addText(text);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas", "textures/particles"), 4);
		renderer.addParticleSystem(new ParticleSystem(10, 0.25f, 0.017f, 1000, particleTexture));
		
		
		//TODO delete audio test code
		renderer.setListenerData(10, 10, 10, 0, 0, 0);//crate game registry

		int buffer = renderer.loadSound("sounds/music/Knightmare_Soundtrack_17.wav");
		int source = renderer.generateSource(1);
		renderer.getAudioHandler().play(source, buffer);
	}

	public Entity geEntity() {
		return q;
	}
	
	public MasterRenderer getRenderer(){
		return renderer;
	}
}
