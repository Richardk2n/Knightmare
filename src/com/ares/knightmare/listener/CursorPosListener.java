package com.ares.knightmare.listener;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.ares.knightmare.entities.Camera;

public class CursorPosListener extends GLFWCursorPosCallback {

	private Camera camera;
	private Timer timer;

	public CursorPosListener(Camera camera) {
		super();
		this.camera = camera;
		timer = new Timer(true);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		timer.cancel();
		timer = new Timer(true);
		if (xpos < 100) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(-2, 0, 0);
				}
			}, 1, 30);
		}
		if (ypos < 100) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(0, -2, 0);
				}
			}, 1, 30);
		}
		if (xpos > camera.getWidth() - 100) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(2, 0, 0);
				}
			}, 1, 30);
		}
		if (ypos > camera.getHeight() - 100) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(0, 2, 0);
				}
			}, 1, 30);
		}
	}
}
