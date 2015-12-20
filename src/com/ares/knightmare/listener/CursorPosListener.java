package com.ares.knightmare.listener;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.ares.knightmare.entities.Camera;

public class CursorPosListener extends GLFWCursorPosCallback {

	private Camera camera;
	private Timer timer;
	
	private static final int amount = 1, period = 20, border = 100;

	public CursorPosListener(Camera camera) {
		super();
		this.camera = camera;
		timer = new Timer(true);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		timer.cancel();
		timer = new Timer(true);
		if (xpos < border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(-amount, 0, 0);
				}
			}, 0, period);
		}
		if (ypos < border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(0, amount, 0);
				}
			}, 0, period);
		}
		if (xpos > camera.getWidth() - border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(amount, 0, 0);
				}
			}, 0, period);
		}
		if (ypos > camera.getHeight() - border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					camera.rotate(0, -amount, 0);
				}
			}, 0, period);
		}
	}
}
