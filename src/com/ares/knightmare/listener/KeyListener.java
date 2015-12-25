package com.ares.knightmare.listener;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.glfw.GLFWKeyCallback;

import com.ares.knightmare.handler.CameraHandler;
import com.ares.knightmare.util.Level;

public class KeyListener extends GLFWKeyCallback {

	private CameraHandler cameraHandler;
	private Level level;
	private Timer timerW, timerS, timerA, timerD, timerSp, timerSh;
	private static final float speed = 0.1f;
	private static final int period = 5;

	public KeyListener(CameraHandler cameraHandler, Level level) {
		super();
		this.cameraHandler = cameraHandler;
		this.level = level;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, GLFW_TRUE);
		}
		if (key == GLFW_KEY_E && action == GLFW_RELEASE) {
			if(cameraHandler.isBound()){
				cameraHandler.bindEntity(null);
			}else{
				cameraHandler.bindEntity(level.geEntity());//TODO
			}
		}

		if (key == GLFW_KEY_W) {
			if (action == GLFW_PRESS) {
				timerW = new Timer(true);
				timerW.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(0, 0, speed);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerW.cancel();
			}
		}
		if (key == GLFW_KEY_S) {
			if (action == GLFW_PRESS) {
				timerS = new Timer(true);
				timerS.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(0, 0, -speed);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerS.cancel();
			}
		}

		if (key == GLFW_KEY_A) {
			if (action == GLFW_PRESS) {
				timerA = new Timer(true);
				timerA.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(-speed, 0, 0);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerA.cancel();
			}
		}
		if (key == GLFW_KEY_D) {
			if (action == GLFW_PRESS) {
				timerD = new Timer(true);
				timerD.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(speed, 0, 0);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerD.cancel();
			}
		}
		
		if (key == GLFW_KEY_SPACE) {
			if (action == GLFW_PRESS) {
				timerSp = new Timer(true);
				timerSp.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(0, speed, 0);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerSp.cancel();
			}
		}
		if (key == GLFW_KEY_LEFT_SHIFT) {
			if (action == GLFW_PRESS) {
				timerSh = new Timer(true);
				timerSh.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						cameraHandler.move(0, -speed, 0);
					}
				}, 0, period);
			} else if (action == GLFW_RELEASE) {
				timerSh.cancel();
			}
		}
	}

}
