package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw, roll;
	private int width, height;
	private boolean ego = true;
	
	public Camera(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void move(float x, float y, float z) {
		position.x += x;
		position.y += (ego?y:-z);
		position.z += (ego?z:y);
	}
	
	public void rotate(float x, float y, float z) {
		if(ego){
			yaw += x;
			pitch += y;
			roll += z;
		}else{
			move(x/100, z/100, y/100);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isEgo() {
		return ego;
	}

	public void setEgo(boolean ego) {
		this.ego = ego;
	}

}
