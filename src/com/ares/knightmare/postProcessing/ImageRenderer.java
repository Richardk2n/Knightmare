package com.ares.knightmare.postProcessing;

import org.lwjgl.opengl.GL11;

import com.ares.knightmare.util.FrameBufferObject;

public class ImageRenderer {

	private FrameBufferObject fbo;

	protected ImageRenderer(int width, int height) {
		fbo = new FrameBufferObject(width, height, FrameBufferObject.NONE);
	}

	protected ImageRenderer() {}

	protected void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	protected int getOutputTexture() {
		return fbo.getColorTexture();
	}

	protected void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
