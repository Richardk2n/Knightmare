package com.ares.knightmare.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Terrain;
import com.ares.knightmare.handler.LightHandler;
import com.ares.knightmare.models.Model;
import com.ares.knightmare.shaders.TerrainShader;
import com.ares.knightmare.textures.TerrainTexturePack;
import com.ares.knightmare.util.Maths;

public class TerrainRenderer {

	private TerrainShader shader = new TerrainShader();
	private LightHandler handler;

	public TerrainRenderer(Matrix4f projectionMatrix, LightHandler handler) {
		shader.start();
		shader.loadProjctionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
		this.handler = handler;
	}

	public void render(List<Terrain> terrains, Matrix4f toShadowMapSpaceMatrix, Vector4f plane, float skyR, float skyG, float skyB, Camera camera, float shadowDistance,
			float transitionDistanceShadow, int shadowPfcCount) {
		shader.start();
		shader.loadClipPlane(plane);
		shader.loadSkyColor(skyR, skyG, skyB);
		shader.loadViewMatrix(camera);
		shader.loadShadowVariables(shadowDistance, transitionDistanceShadow, shadowPfcCount);
		shader.loadToShadowMapSpaceMatrix(toShadowMapSpaceMatrix);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
		shader.stop();
	}

	private void prepareTerrain(Terrain terrain) {
		Model model = terrain.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}

	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap());
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadLights(handler.getNearestLights(terrain.getCentralPosition()));// TODO
																					// switch
																					// for
																					// camera?
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
