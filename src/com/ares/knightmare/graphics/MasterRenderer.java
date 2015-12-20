package com.ares.knightmare.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.shaders.StaticShader;
import com.ares.knightmare.shaders.TerrainShader;
import com.ares.knightmare.terrain.Terrain;

public class MasterRenderer {

	private static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000, SKY_R = 0.4f, SKY_G = 0.5f, SKY_B = 0.5f;

	private StaticShader staticShader = new StaticShader();
	private EntityRenderer entityRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;

	private Matrix4f projectionMatrix;

	private int width, height;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();

	public MasterRenderer(int width, int height) {
		this.width = width;
		this.height = height;
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
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
		glClearColor(SKY_R, SKY_G, SKY_B, 0.0f);
	}

	public void render(Light sun, Camera camera) {
		staticShader.start();
		staticShader.loadSkyColor(SKY_R, SKY_G, SKY_B);
		staticShader.loadLight(sun);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		terrainShader.start();
		terrainShader.loadSkyColor(SKY_R, SKY_G, SKY_B);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		entities.clear(); // TODO change up why per frame??
		terrains.clear();
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch == null) {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		} else {
			batch.add(entity);
		}
	}

	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
