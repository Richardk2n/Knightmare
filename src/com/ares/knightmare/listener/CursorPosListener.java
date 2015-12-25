package com.ares.knightmare.listener;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.ares.knightmare.handler.CameraHandler;

public class CursorPosListener extends GLFWCursorPosCallback {

	private CameraHandler cameraHandler;
	private Timer timer;
	
	private static final int amount = 1, period = 20, border = 100;

	public CursorPosListener(CameraHandler cameraHandler) {
		super();
		this.cameraHandler = cameraHandler;
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
					cameraHandler.rotate(-amount, 0, 0);
				}
			}, 0, period);
		}
		if (ypos < border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					cameraHandler.rotate(0, amount, 0);
				}
			}, 0, period);
		}
		if (xpos > cameraHandler.getWidth() - border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					cameraHandler.rotate(amount, 0, 0);
				}
			}, 0, period);
		}
		if (ypos > cameraHandler.getHeight() - border) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					cameraHandler.rotate(0, -amount, 0);
				}
			}, 0, period);
		}
	}
}
