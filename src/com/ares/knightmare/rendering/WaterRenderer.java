package com.ares.knightmare.rendering;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.WaterTile;
import com.ares.knightmare.handler.LightHandler;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.shaders.WaterShader;
import com.ares.knightmare.util.Maths;
import com.ares.knightmare.util.WaterFrameBuffers;

public class WaterRenderer {

	private static final String DUDV_MAP = "waterDUDV";
	private static final String NORMAL_MAP = "matchingNormalMap";
	
	private final RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	
	private int dudvTexture;
	private int normalTexture;
	
	private LightHandler handler;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, Loader loader, WaterFrameBuffers fbos, LightHandler handler) {
		float[] positions = { 1, -1, -1, -1, 1, 1, -1, 1 };
		quad = loader.loadToVAO(positions, 2);
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(DUDV_MAP, "maps/dudv");
		normalTexture = loader.loadTexture(NORMAL_MAP, "maps/normal");
		shader.start();
		shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        this.handler = handler;
	}

	public void render(List<WaterTile> waters, Camera camera) {
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.loadShineVariables(0.5f, 0.1f);
		for (WaterTile water : waters) {
			shader.loadLights(handler.getNearestLights(water.getCentralPosition()));//TODO switch for camera?
			shader.loadMoveFactor(water.getMoveFactor());
			Matrix4f matrix = Maths.createTransformationMatrix(water.getPosition(), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
