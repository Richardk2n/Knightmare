package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float yz, xz, roll;
	private int width, height;
	private boolean ego = true;

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void move(float ad, float ss, float ws) {
		if (ego) {
			position.x += ad * Math.cos(Math.toRadians(xz)) + ws * Math.sin(Math.toRadians(xz));
			position.y += ss; // TODO up and down
			position.z -= ws * Math.cos(Math.toRadians(xz)) - ad * Math.sin(Math.toRadians(xz));
		} else {
			position.x += ad;// TODO both
			position.y += ss;
			position.z -= ws;
		}
	}

	public void set(float x, float y, float z, float yz, float xz, float roll) {
		position.x = x;
		position.y = y;
		position.z = z;
		this.yz = yz;
		this.xz = xz;
		this.roll = roll;
	}

	public void rotate(float xz, float yz, float xy) {
		if (ego) {
			this.xz += xz;
			this.yz -= yz;
			if(this.yz >90){
				this.yz = 90; 
			}else if(this.yz<-90){
				this.yz=-90;
			}
			roll += xy;
		} else {
			move(xz, xy, yz);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return yz;
	}

	public float getYaw() {
		return xz;
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
