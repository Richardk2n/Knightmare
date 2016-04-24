package com.ares.knightmare.rendering;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.entities.Particle;
import com.ares.knightmare.entities.ParticleSystem;
import com.ares.knightmare.entities.Terrain;
import com.ares.knightmare.entities.WaterTile;
import com.ares.knightmare.handler.AudioHandler;
import com.ares.knightmare.handler.CameraHandler;
import com.ares.knightmare.handler.EntityHandler;
import com.ares.knightmare.handler.LightHandler;
import com.ares.knightmare.handler.NormalMappedEntityHandler;
import com.ares.knightmare.handler.ParticleHandler;
import com.ares.knightmare.handler.TerrainHandler;
import com.ares.knightmare.handler.TextHandler;
import com.ares.knightmare.handler.TimeHandler;
import com.ares.knightmare.handler.WaterHandler;
import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.shadows.ShadowMapMasterRenderer;
import com.ares.knightmare.textures.GuiTexture;
import com.ares.knightmare.util.FrameBufferObject;
import com.ares.knightmare.util.fontMeshCreator.GUIText;

public class MasterRenderer {

	public static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	public static final float SKY_R = 0.4f, SKY_G = 0.5f, SKY_B = 0.5f;
	private static final float SHADOW_DISTANCE = 150, TRANSITION_DISTANCE_SHADOW = 10;
	private static final int SHADOW_PFC_COUNT = 1; //TODO settings

	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	private GuiRenderer guiRenderer;
	private SkyboxRenderer skyboxRenderer;
	private WaterRenderer waterRenderer;
	private ParticleRenderer particleRenderer;
	private NormalMappingRenderer normalMappingRenderer;
	private ShadowMapMasterRenderer shadowMapMasterRenderer;

	private TimeHandler timeHandler;

	private EntityHandler entityHandler = new EntityHandler();
	private NormalMappedEntityHandler normalMappedEntityHandler = new NormalMappedEntityHandler();
	private LightHandler lightHandler = new LightHandler();
	private TerrainHandler terrainHandler = new TerrainHandler();
	private WaterHandler waterHandler = new WaterHandler();
	private ParticleHandler particleHandler = new ParticleHandler(1);// TODO
	private AudioHandler audioHandler = new AudioHandler();
	private TextHandler textHandler;

	private Matrix4f projectionMatrix;

	public static int width, height;

	private List<GuiTexture> guis = new ArrayList<>();

	public MasterRenderer(int width, int height, Loader loader, FrameBufferObject reflectionFrameBuffer, FrameBufferObject refractionFrameBuffer,
			CameraHandler cameraHandler) {
		MasterRenderer.width = width;
		MasterRenderer.height = height;
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(projectionMatrix, lightHandler);
		terrainRenderer = new TerrainRenderer(projectionMatrix, lightHandler);
		guiRenderer = new GuiRenderer(loader);
		skyboxRenderer = new SkyboxRenderer(projectionMatrix, loader);
		waterRenderer = new WaterRenderer(projectionMatrix, loader, reflectionFrameBuffer, refractionFrameBuffer, lightHandler);
		particleRenderer = new ParticleRenderer(loader, projectionMatrix);

		normalMappingRenderer = new NormalMappingRenderer(projectionMatrix, lightHandler);

		shadowMapMasterRenderer = new ShadowMapMasterRenderer();

		timeHandler = new TimeHandler(entityHandler, normalMappedEntityHandler, lightHandler, terrainHandler, waterHandler, particleHandler, cameraHandler);
		textHandler = new TextHandler(loader);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		glClearColor(SKY_R, SKY_G, SKY_B, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}

	public void renderScene(Camera camera, Vector4f plane) {
		entityRenderer.render(entityHandler.getRenderedEntities(camera), plane, SKY_R, SKY_G, SKY_B, camera);

		normalMappingRenderer.render(normalMappedEntityHandler.getRenderedEntities(camera), plane, camera);

		terrainRenderer.render(terrainHandler.getRenderedTerrains(camera), shadowMapMasterRenderer.getToShadowMapSpaceMatrix(), plane, SKY_R, SKY_G, SKY_B, camera,
				SHADOW_DISTANCE, TRANSITION_DISTANCE_SHADOW, SHADOW_PFC_COUNT);

		skyboxRenderer.render(camera, SKY_R, SKY_G, SKY_B);
	}

	public void renderWaterAndParticles(Camera camera) {
		waterRenderer.render(waterHandler.getRenderedWaters(camera), camera, NEAR_PLANE, FAR_PLANE, SKY_R, SKY_G, SKY_B);

		try {
			particleHandler.acquire();//TODO Maybe try as not this important
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		particleRenderer.render(particleHandler.getRenderedParticles(camera), camera);
		particleHandler.release();
	}

	public void renderGui() {
		guiRenderer.render(guis);
	}

	public void renderText() {
		textHandler.render();
	}

	public void renderShadowMap(Camera camera) {
		shadowMapMasterRenderer.render(entityHandler.getRenderedEntities(camera), normalMappedEntityHandler.getRenderedEntities(camera), lightHandler.getSun(), camera);
	}

	public void addTerrain(Terrain terrain) {
		terrainHandler.store(terrain);
	}

	public void removeTerrain(Terrain terrain) {
		terrainHandler.remove(terrain);
	}

	public void addGui(GuiTexture gui) {
		guis.add(gui);
	}

	public void removeGui(GuiTexture gui) {
		guis.remove(gui);
	}

	public void addLight(Light light) {
		lightHandler.store(light);
	}

	public void removeLight(Light light) {
		lightHandler.remove(light);
	}

	public void addSun(Light sun) {
		lightHandler.setSun(sun);
	}

	public void removeSun() {
		lightHandler.setSun(null);
	}

	public void addEntity(Entity entity) {
		entityHandler.store(entity);
	}

	public void removeEntity(Entity entity) {
		entityHandler.remove(entity);
	}

	public void addNormalMappedEntity(Entity entity) {
		normalMappedEntityHandler.store(entity);
	}

	public void removeNormalMappedEntity(Entity entity) {
		normalMappedEntityHandler.remove(entity);
	}

	public void addWater(WaterTile water) {
		waterHandler.store(water);
	}

	public void removeWater(WaterTile water) {
		waterHandler.remove(water);
	}

	public void addParticle(Particle particle) {
		particleHandler.store(particle);
	}

	public void removeParticle(Particle particle) {
		particleHandler.remove(particle);
	}

	public void addParticleSystem(ParticleSystem particleSystem) {
		particleHandler.store(particleSystem);
	}

	public void removeParticleSystem(ParticleSystem particleSystem) {
		particleHandler.remove(particleSystem);
	}

	public void cleanUp() {
		entityRenderer.cleanUp();
		terrainRenderer.cleanUp();
		guiRenderer.cleanUp();
		skyboxRenderer.cleanUp();
		waterRenderer.cleanUp();
		normalMappingRenderer.cleanUp();
		shadowMapMasterRenderer.cleanUp();
		audioHandler.cleanUp();
		textHandler.cleanUp();
	}

	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public TerrainHandler getTerrainHandler() {
		return terrainHandler;
	}

	public int getShadowMapTexture() {
		return shadowMapMasterRenderer.getShadowMap();
	}

	public void setListenerData(float x, float y, float z, float rotX, float rotY, float rotZ) {// TODO
																								// world
																								// handler
																								// audio
																								// ->
																								// game
																								// cycle
		audioHandler.setListenerData(x, y, z, rotX, rotY, rotZ);
	}

	public int loadSound(String path) {
		return audioHandler.loadSound(path);
	}

	public int generateSource(float volume) {// TODO set volume 1 maybe
		return audioHandler.generateSource(volume);
	}
	
	public AudioHandler getAudioHandler(){
		return audioHandler;
	}

	public void addText(GUIText text) {
		textHandler.loadText(text);
	}

	public void removeText(GUIText text) {
		textHandler.removeText(text);
	}
}
