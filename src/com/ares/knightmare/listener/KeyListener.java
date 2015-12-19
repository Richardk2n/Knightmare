package com.ares.knightmare.listener;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

import com.ares.knightmare.entities.Camera;

public class KeyListener extends GLFWKeyCallback {

	private Camera camera;

	public KeyListener(Camera camera) {
		super();
		this.camera = camera;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, GLFW_TRUE);
		} else if (key == GLFW_KEY_W && action != GLFW_RELEASE) {
			camera.move(0, 0, -0.2f);
		} else if (key == GLFW_KEY_S && action != GLFW_RELEASE) {
			camera.move(0, 0, 0.2f);
		} else if (key == GLFW_KEY_A && action != GLFW_RELEASE) {
			camera.move(-0.2f, 0, 0);
		} else if (key == GLFW_KEY_D && action != GLFW_RELEASE) {
			camera.move(0.2f, 0, 0);
		}
	}

}