package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	protected Vector3f position = new Vector3f(0, 0, 0);
	protected float rotX, rotY, rotZ;
	private int width, height;

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void move(float ad, float ss, float ws) {
		position.x += ad * Math.cos(Math.toRadians(rotY)) + ws * Math.sin(Math.toRadians(rotY));
		position.y += ss; // TODO up and down
		position.z -= ws * Math.cos(Math.toRadians(rotY)) - ad * Math.sin(Math.toRadians(rotY));
	}

	public Camera set(float x, float y, float z, float rotX, float rotY, float rotZ) {
		position.x = x;
		position.y = y;
		position.z = z;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		return this;
	}

	public void rotate(float rotX, float rotY, float rotZ) {
		this.rotY += rotY;
		this.rotX -= rotX;
		if (this.rotX > 90) {
			this.rotX = 90;
		} else if (this.rotX < -90) {
			this.rotX = -90;
		}
		this.rotZ += rotZ;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void invertRotX(){
		rotX = -rotX;
	}
	
	public Camera clone(){
		return new Camera(width, height).set(position.x, position.y, position.z, rotX, rotY, rotZ);
	}

}
