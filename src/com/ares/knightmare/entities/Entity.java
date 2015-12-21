package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.models.TexturedModel;

public class Entity {

	private TexturedModel model;
	private Vector3f position, cameraPosition;
	private float yz, xz, roll, scale, distance = 10, height;;
	private Camera camera;

	public Entity(TexturedModel model, Vector3f position, float xz, float yz, float roll, float scale) {
		this.model = model;
		this.position = position;
		this.yz = yz;
		this.xz = xz;
		this.roll = roll;
		this.scale = scale;
		height = model.getRawModel().getHeight();
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

	public boolean hasCamera() {
		return camera != null;
	}

	public void move(float ad, float ss, float ws) {
		position.x += ad * Math.cos(Math.toRadians(xz)) - ws * Math.sin(Math.toRadians(xz));
		position.y += ss; // TODO up and down
		position.z += ws * Math.cos(Math.toRadians(xz)) + ad * Math.sin(Math.toRadians(xz));
		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

	public void rotate(float xz, float yz, float xy) {
		this.xz += xz;
		this.yz -= yz;
		if (this.yz > 90) {
			this.yz = 90;
		} else if (this.yz < -90) {
			this.yz = -90;
		}
		roll += xy;
		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

	private float[] calculateCam(float distance) {
		float[] args = new float[6];
		args[0] = yz;
		args[1] = xz;
		args[2] = roll;
		float sxz = (float) (distance * Math.cos(Math.toRadians(yz)));
		args[3] = -(float) (sxz * Math.sin(Math.toRadians(xz)));
		args[4] = (float) (distance * Math.sin(Math.toRadians(yz)));
		args[5] = (float) (sxz * Math.cos(Math.toRadians(xz)));
		cameraPosition = getTop(position);
		return args;
	}

	private Vector3f getTop(Vector3f bottom) {//TODO test if both
		float s = (float) (height * Math.cos(Math.toRadians(90+yz)));
		return new Vector3f((float) (bottom.x + s * Math.sin(Math.toRadians(xz))), (float) (bottom.y + height * Math.sin(Math.toRadians(90+yz))),
				(float) (bottom.z + s * Math.cos(Math.toRadians(xz))));
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Camera getCamera() {
		return camera;
	}

	public float getYz() {
		return yz;
	}

	public void setYz(float yz) {
		this.yz = yz;
	}

	public float getXz() {
		return xz;
	}

	public void setXz(float xz) {
		this.xz = xz;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public void zoom(float distance) {
		this.distance -= distance;
		if (this.distance < 0) {
			this.distance = 0;
		}
		float[] args = calculateCam(this.distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

}
