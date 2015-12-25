package com.ares.knightmare.listener;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.ares.knightmare.handler.CameraHandler;

public class MouseButtonListener extends GLFWMouseButtonCallback{

	private CameraHandler cameraHandler;
	
	public MouseButtonListener(CameraHandler cameraHandler) {
		super();
		this.cameraHandler = cameraHandler;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		// TODO Auto-generated method stub
		
	}
}
