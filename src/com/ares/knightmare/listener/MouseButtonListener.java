package com.ares.knightmare.listener;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Particle;
import com.ares.knightmare.handler.CameraHandler;
import com.ares.knightmare.util.Level;

public class MouseButtonListener extends GLFWMouseButtonCallback {

	private CameraHandler cameraHandler;
	private MousePicker mousePicker;
	private Level level;

	public MouseButtonListener(CameraHandler cameraHandler, MousePicker mousePicker, Level level) {
		this.cameraHandler = cameraHandler;
		this.mousePicker = mousePicker;
		this.level = level;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		// TODO Auto-generated method stub
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
//			level.getRenderer().addParticle(new Particle(new Vector3f(cameraHandler.getCamera().getPosition()),
//					mousePicker.getRay((float) CursorPosListener.getPosX(), (float) CursorPosListener.getPosY(), cameraHandler.getCamera()), 0, 100, 0, 1));TODO
		}
	}
}
