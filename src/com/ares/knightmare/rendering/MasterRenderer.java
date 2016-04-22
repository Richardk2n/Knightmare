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
import com.ares.knightmare.entities.WaterTile;
import com.ares.knightmare.handler.CameraHandler;
import com.ares.knightmare.handler.EntityHandler;
import com.ares.knightmare.handler.LightHandler;
import com.ares.knightmare.handler.NormalMappedEntityHandler;
import com.ares.knightmare.handler.ParticleHandler;
import com.ares.knightmare.handler.TerrainHandler;
import com.ares.knightmare.handler.TimeHandler;
import com.ares.knightmare.handler.WaterHandler;
import com.ares.knightmare.normalMapping.NormalMappingRenderer;
import com.ares.knightmare.particles.Particle;
import com.ares.knightmare.particles.ParticleRenderer;
import com.ares.knightmare.particles.ParticleShader;
import com.ares.knightmare.particles.ParticleSystem;
import com.ares.knightmare.shaders.GuiShader;
import com.ares.knightmare.shaders.SkyboxShader;
import com.ares.knightmare.shaders.EntityShader;
import com.ares.knightmare.shaders.TerrainShader;
import com.ares.knightmare.shaders.WaterShader;
import com.ares.knightmare.shadows.ShadowMapMasterRenderer;
import com.ares.knightmare.terrain.Terrain;
import com.ares.knightmare.util.GuiTexture;
import com.ares.knightmare.util.WaterFrameBuffers;

public class MasterRenderer {

	public static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	public static final float SKY_R = 0.4f, SKY_G = 0.5f, SKY_B = 0.5f;

	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	private GuiShader guiShader = new GuiShader();
	private GuiRenderer guiRenderer;
	private SkyboxShader skyboxShader = new SkyboxShader();
	private SkyboxRenderer skyboxRenderer;
	private WaterShader waterShader = new WaterShader();
	private WaterRenderer waterRenderer;
	private ParticleShader particleShader = new ParticleShader();
	private ParticleRenderer particleRenderer;
	
	private NormalMappingRenderer normalMappingRenderer;
	
	private ShadowMapMasterRenderer shadowMapMasterRenderer;
	
	private TimeHandler timeHandler;
	
	private EntityHandler entityHandler = new EntityHandler();
	private NormalMappedEntityHandler normalMappedEntityHandler = new NormalMappedEntityHandler();
	private LightHandler lightHandler = new LightHandler();
	private TerrainHandler terrainHandler = new TerrainHandler();
	private WaterHandler waterHandler = new WaterHandler();
	private ParticleHandler particleHandler = new ParticleHandler(1);

	private Matrix4f projectionMatrix;

	public static int width, height;

	private List<GuiTexture> guis = new ArrayList<>();

	public MasterRenderer(int width, int height, Loader loader, WaterFrameBuffers fbos, CameraHandler cameraHandler) {
		MasterRenderer.width = width;
		MasterRenderer.height = height;
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix, lightHandler);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix, lightHandler);
		guiRenderer = new GuiRenderer(guiShader, loader);
		skyboxRenderer = new SkyboxRenderer(skyboxShader, projectionMatrix, loader);
		waterRenderer = new WaterRenderer(waterShader, projectionMatrix, loader, fbos, lightHandler);
		particleRenderer = new ParticleRenderer(loader, projectionMatrix);

		normalMappingRenderer = new NormalMappingRenderer(projectionMatrix, lightHandler);
		
		shadowMapMasterRenderer = new ShadowMapMasterRenderer();
		
		timeHandler = new TimeHandler(entityHandler, normalMappedEntityHandler, lightHandler, terrainHandler, waterHandler, particleHandler, cameraHandler);
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
		//entities
		entityShader.start();
		entityShader.loadClipPlane(plane);
		entityShader.loadSkyColor(SKY_R, SKY_G, SKY_B);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entityHandler.getRenderedEntities(camera));
		entityShader.stop();
		
		//normal mapped entities
		normalMappingRenderer.render(normalMappedEntityHandler.getRenderedEntities(camera), plane, camera);
		
		//terrain
		terrainShader.start();
		terrainShader.loadClipPlane(plane);
		terrainShader.loadSkyColor(SKY_R, SKY_G, SKY_B);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrainHandler.getRenderedTerrains(camera), shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		//skybox
		skyboxShader.start();
		skyboxRenderer.render(camera, SKY_R, SKY_G, SKY_B);
		skyboxShader.stop();
	}
	
	public void renderWaterAndParticles(Camera camera){
		//water
		waterShader.start();
		waterShader.loadPlanes(NEAR_PLANE, FAR_PLANE);
		waterShader.loadSkyColor(SKY_R, SKY_G, SKY_B);
		waterRenderer.render(waterHandler.getRenderedWaters(camera), camera);
		waterShader.stop();
		
		//particles
		try {
			particleHandler.acquire();//Maybe try as not this important
		} catch (InterruptedException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
		particleRenderer.render(particleHandler.getRenderedParticles(camera), camera);
		particleHandler.release();
	}
	
	public void renderGui(){
		guiShader.start();
		guiRenderer.render(guis);
		guiShader.stop();
	}
	
	public void renderShadowMap(Camera camera){
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
		entityShader.cleanUp();
		terrainShader.cleanUp();
		guiShader.cleanUp();
		skyboxShader.cleanUp();
		waterShader.cleanUp();
		particleShader.cleanUp();
		normalMappingRenderer.cleanUp();
		shadowMapMasterRenderer.cleanUp();
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
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public TerrainHandler getTerrainHandler(){
		return terrainHandler;
	}
	
	public int getShadowMapTexture(){
		return shadowMapMasterRenderer.getShadowMap();
	}
}
