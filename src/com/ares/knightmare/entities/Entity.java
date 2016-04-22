package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.handler.TerrainHandler;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.util.Maths;

public class Entity {

	private TexturedModel model;
	private Vector3f position, cameraPosition;
	private float rotX, rotY, rotZ, scale, distance = 10, xSpeed, ySpeed, zSpeed, BRAKE = -0.03f;// TODO
																									// use
	public static final float GRAVITY = -0.03f; // TODO rework
	private Camera camera;
	private int textureIndex;

	public Entity(TexturedModel model, Vector3f position, float rotY, float rotX, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public Entity(TexturedModel model, Vector3f position, float rotY, float rotX, float rotZ, float scale, int textureIndex) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

	public boolean hasCamera() {
		return camera != null;
	}

	public void tick(TerrainHandler terrainHandler) {
		position.y += ySpeed;
		ySpeed += GRAVITY;
		float terrainHeight = terrainHandler.getHeightOfTerrain(position.x, position.z);
		if (position.y < terrainHeight) {
			ySpeed = 0;
			position.y = terrainHeight;
		}
		if (camera != null) {
			float[] args = calculateCam(distance);
			camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
		}
	}

	public void move(float ad, float ss, float ws) {
		position.x += ad * Math.cos(Math.toRadians(rotY)) - ws * Math.sin(Math.toRadians(rotY));
		if (ySpeed == 0) {
			ySpeed = ss * 7;
		}
		position.z += ws * Math.cos(Math.toRadians(rotY)) + ad * Math.sin(Math.toRadians(rotY));

		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
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
		
		this.rotX %= 360;
		this.rotY %= 360;
		if(this.rotY>180){
			this.rotY -=360;//TODO
		}
		if(this.rotY<-180){
			this.rotY += 360;
		}
		this.rotZ %= 360;
		
		float[] args = calculateCam(distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);
	}

	private float[] calculateCam(float distance) {
		float[] args = new float[6];
		args[0] = rotX;
		args[1] = rotY;
		args[2] = rotZ;
		float sxz = (float) (distance * Math.cos(Math.toRadians(rotX)));
		args[3] = -(float) (sxz * Math.sin(Math.toRadians(rotY)));
		args[4] = (float) (distance * Math.sin(Math.toRadians(rotX)));
		args[5] = (float) (sxz * Math.cos(Math.toRadians(rotY)));
		cameraPosition = getTop();
		return args;
	}

	private Vector3f getTop() {
		float s = (float) (model.getRawModel().getToTop() * Math.cos(Math.toRadians(90 + rotX)));
		return new Vector3f((float) (position.x - scale * s * Math.sin(Math.toRadians(rotY))),
				(float) (position.y + scale * model.getRawModel().getToTop() * Math.sin(Math.toRadians(90 + rotX))),
				(float) (position.z + scale * s * Math.cos(Math.toRadians(rotY))));
	}

	public float getTextureXOffset() {
		int number = model.getTexture().getNumberOfRows();
		int column = textureIndex % number;
		return (float) column / (float) number;
	}

	public float getTextureYOffset() {
		int number = model.getTexture().getNumberOfRows();
		int row = textureIndex / number;
		return (float) row / (float) number;
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

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void zoom(float distance) {
		this.distance -= distance;
		if (this.distance < 0) {
			this.distance = 0;
		}
		float[] args = calculateCam(this.distance);
		camera.set(cameraPosition.x + args[3], cameraPosition.y + args[4], cameraPosition.z + args[5], args[0], args[1], args[2]);

		Vector3f[] corners = getCorners();// TODO
	}

	public Vector3f[] getCorners() {
		RawModel model = this.model.getRawModel();
		return Maths.calculateCorners(position, new Vector3f(model.getWidth(), model.getHeight(), model.getDepth()),
				new Vector3f(model.getToRight(), model.getToTop(), model.getToEnd()), new Vector3f(rotX, rotY, rotZ));
	}

	public void screem() {
		System.out.println("You got me!");
	}

}
