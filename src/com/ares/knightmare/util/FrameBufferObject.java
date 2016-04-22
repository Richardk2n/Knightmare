package com.ares.knightmare.util;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.ares.knightmare.rendering.MasterRenderer;

public class FrameBufferObject {

	public static final int NONE = 0, COLOR_TEXTURE = 1, DEPTH_TEXTURE = 2, BOTH = 3;

	private final int width, height;

	private int frameBuffer, colorTexture, depthTexture, deapthBuffer;

	public FrameBufferObject(int width, int height, int attachmentType) {
		this.width = width;
		this.height = height;
		initialiseFrameBuffer(attachmentType);
	}

	private void initialiseFrameBuffer(int type) {
		createFrameBuffer();
		createDepthBufferAttachment();
		if (type == COLOR_TEXTURE) {
			createColorTextureAttachment();
		} else if (type == DEPTH_TEXTURE) {
			createDepthTextureAttachment();
		} else if (type == BOTH) {
			createColorTextureAttachment();
			createDepthTextureAttachment();
		}
		unbindFrameBuffer();
	}

	private void createFrameBuffer() {
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	public void bindFrameBuffer() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	public void unbindFrameBuffer() {//TODO move
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, MasterRenderer.width, MasterRenderer.height);
	}

	private void createColorTextureAttachment() {
		colorTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture, 0);
	}
	
	private void createDepthTextureAttachment() {
		depthTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
	}
	
	public void cleanUp() {
		GL30.glDeleteFramebuffers(frameBuffer);
		GL30.glDeleteFramebuffers(deapthBuffer);
		GL11.glDeleteTextures(colorTexture);
		GL11.glDeleteTextures(depthTexture);
	}
	
	public int getDepthTexture(){
		return depthTexture;
	}
	
	public int getColorTexture(){
		return colorTexture;
	}
	
	private int createDepthBufferAttachment() {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}
}
