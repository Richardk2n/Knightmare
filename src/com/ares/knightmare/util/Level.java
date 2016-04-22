package com.ares.knightmare.util;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.EntityFactory;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.entities.ParticleSystem;
import com.ares.knightmare.entities.Source;
import com.ares.knightmare.entities.Terrain;
import com.ares.knightmare.entities.WaterTile;
import com.ares.knightmare.handler.AudioMaster;
import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.loader.NormalMappedObjLoader;
import com.ares.knightmare.loader.OBJLoader;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.rendering.MasterRenderer;
import com.ares.knightmare.textures.GuiTexture;
import com.ares.knightmare.textures.ModelTexture;
import com.ares.knightmare.textures.ParticleTexture;
import com.ares.knightmare.textures.TerrainTexture;
import com.ares.knightmare.textures.TerrainTexturePack;
import com.ares.knightmare.util.fontMeshCreator.FontType;
import com.ares.knightmare.util.fontMeshCreator.GUIText;

public class Level {

	private Entity q;
	private MasterRenderer renderer;

	public Level(MasterRenderer renderer, Loader loader) {
		this.renderer = renderer;
		Entity entity = EntityFactory.createEntity(loader, "lamp", "lamp", "textures", false, new Vector3f(35, 0, -35), new Vector3f(0, 0, 0), 1, 1, 0);

		RawModel modelg = OBJLoader.loadObjModel("fern", loader);
		TexturedModel texturedModelg = new TexturedModel(modelg, new ModelTexture(loader.loadTexture("fern", "textures")));
		ModelTexture textureg = texturedModelg.getTexture();
		textureg.setNumberOfRows(2);
		textureg.setShineDamper(0);
		textureg.setReflectivity(0);
		textureg.setHasTransparency(true);
		Entity gras = new Entity(texturedModelg, new Vector3f(25, 0, -25), 0, 0, 0, 1, 3);

		q = EntityFactory.createEntity(loader, "tree", "tree", "textures", true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 2, 0, 0);
		
		Entity fern2 = EntityFactory.createEntity(loader, "fern", "fern", "textures", true, new Vector3f(-10, 15, 10), new Vector3f(0, 0, 0), 1, 0, 0, 2, 0);

		// TODO
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("gras", "textures"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("white", "textures"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("Mauer", "textures"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("sand", "textures"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "maps/blend"));
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
		Light light4 = new Light(new Vector3f(35, entity.getModel().getRawModel().getHeight() * 2 - 10, -35), new Vector3f(2, 0, 0), new Vector3f(0.01f, 0.01f, 0.001f));// TODO
		// renderer.addLight(light);
		// renderer.addLight(light1);
//		 renderer.addLight(light2);
		renderer.addLight(light3);
		renderer.addLight(light4);
		renderer.addSun(light);

		WaterTile water = new WaterTile(50, -50, 0);
		renderer.addWater(water);
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel", "textures")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal", "maps/normal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		renderer.addNormalMappedEntity(new Entity(barrelModel, new Vector3f(0, 8, 40), 0, 0, 0, 0.25f));
		
		FontType font = new FontType(loader.loadTexture("candara", "textures/font"), "/textures/font/candara.fnt");
		GUIText text = new GUIText("Hello, you fool! Have a nice day.", 3, font, new Vector2f(0.125f, 0.125f), 0.75f, true);
		text.setColour(1, 0, 0);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas", "textures/particles"), 4);
//		renderer.addParticle(new Particle(new Vector3f(q.getPosition()), new Vector3f(0, 0.5f, 0), 0.25f, 1000, 0, 1, particleTexture));
		renderer.addParticleSystem(new ParticleSystem(10, 0.25f, 0.017f, 1000, particleTexture));
		
		
		//TODO delete audio test code
		AudioMaster.init();
		AudioMaster.setListenerData(10, 10, 10, 0, 0, 0);

		int buffer = AudioMaster.loadSound("sounds/music/Knightmare_Soundtrack_17.wav");
		Source source = AudioMaster.generateSource(1);
		source.play(buffer);
	}

	public Entity geEntity() {
		return q;
	}
	
	public MasterRenderer getRenderer(){
		return renderer;
	}
}
