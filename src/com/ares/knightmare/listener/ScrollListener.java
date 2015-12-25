package com.ares.knightmare.listener;

import org.lwjgl.glfw.GLFWScrollCallback;

import com.ares.knightmare.handler.CameraHandler;

public class ScrollListener extends GLFWScrollCallback{

	private CameraHandler cameraHandler;
	
	public ScrollListener(CameraHandler cameraHandler) {
		super();
		this.cameraHandler = cameraHandler;
	}
	
	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		cameraHandler.zoom(yoffset);
	}

}
