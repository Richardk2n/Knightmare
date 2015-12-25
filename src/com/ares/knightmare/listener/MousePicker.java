package com.ares.knightmare.listener;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.util.Maths;

public class MousePicker {

	private Matrix4f projectionMatrix;
	private int width, height;

	public MousePicker(Matrix4f projection, int width, int height) {
		projectionMatrix = projection;
		this.width = width;
		this.height = height;
	}

	public Vector3f getRay(float mouseX, float mouaseY, Camera camera) {
		return calculateMouseRay(mouseX, mouaseY, camera);
	}

	private Vector3f calculateMouseRay(float mouseX, float mouseY, Camera camera) {
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords, camera);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords, Camera camera) {
		Matrix4f invertedView = Matrix4f.invert(Maths.createViewMatrix(camera), null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / width - 1f;
		float y = (2.0f * mouseY) / height - 1f;
		return new Vector2f(x, y);
	}

}