package com.ares.knightmare.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.shaders.SkyboxShader;

public class SkyboxRenderer {

	private static final int MILLIS_PER_DAY = 50000;
	private static final float SIZE = 500f;
	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE };
	private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" },
			NIGHT_TEXTURE_FILES = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront" };
	private RawModel cube;
	private int texture, nightTexture;
	private SkyboxShader shader;

	public SkyboxRenderer(SkyboxShader shader, Matrix4f projectionMatrix, Loader loader) {
		this.shader = shader;
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures(){
		int oclock = shader.getTime() % MILLIS_PER_DAY;
		float factor = (float) (Math.cos(((double) oclock/(double) MILLIS_PER_DAY)*2*Math.PI)/(double) 2 +0.5);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		shader.loadBlendFactor(factor);
	}
}
