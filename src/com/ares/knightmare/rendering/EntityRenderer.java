package com.ares.knightmare.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.handler.LightHandler;
import com.ares.knightmare.models.Model;
import com.ares.knightmare.shaders.EntityShader;
import com.ares.knightmare.textures.ModelTexture;
import com.ares.knightmare.util.Maths;

public class EntityRenderer {

	private EntityShader shader = new EntityShader();
	private LightHandler handler;

	public EntityRenderer(Matrix4f projectionMatrix, LightHandler handler) {
		shader.start();
		shader.loadProjctionMatrix(projectionMatrix);
		shader.stop();
		this.handler = handler;
	}

	public void render(Map<Model, List<Entity>> entities, Vector4f plane, float skyR, float skyG, float skyB, Camera camera) {
		shader.start();
		shader.loadClipPlane(plane);
		shader.loadSkyColor(skyR, skyG, skyB);
		shader.loadViewMatrix(camera);
		for (Model model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}

	private void prepareTexturedModel(Model texturedModel) {
		GL30.glBindVertexArray(texturedModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = texturedModel.getTexture();
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadNumberOfRows(texture.getNumberOfRows());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), -entity.getRotX(), -entity.getRotY(), -entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
		shader.loadLights(handler.getNearestLights(entity.getPosition()));
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
