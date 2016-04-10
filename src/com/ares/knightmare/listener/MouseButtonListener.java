package com.ares.knightmare.listener;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.EntityFactory;
import com.ares.knightmare.handler.CameraHandler;

public class MouseButtonListener extends GLFWMouseButtonCallback{

	private CameraHandler cameraHandler;
	private MousePicker mousePicker;
	
	public MouseButtonListener(CameraHandler cameraHandler, MousePicker mousePicker) {
		this.cameraHandler = cameraHandler;
		this.mousePicker = mousePicker;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		// TODO Auto-generated method stub
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT){
			System.out.println(mousePicker.getRay((float) CursorPosListener.getPosX(), (float) CursorPosListener.getPosY(), cameraHandler.getCamera()));
			
		}
	}
}
