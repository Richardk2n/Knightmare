package com.ares.knightmare.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import com.ares.knightmare.entities.Camera;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	private static Vector3f[] calculatePoints(float x, float y, float z, float toRight, float toTop, float toEnd, float rotX, float rotY, float rotZ) {
		Vector3f[] points = new Vector3f[8];
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 4; i++) {
				points[j * 4 + i] = rotateVector(
						new Vector3f(toRight - (x * (i % 3) / (i == 0 ? 1 : i)), toTop - j * y, toEnd - (z * (float) Math.floor(i / (double) 2))), rotX, rotY, rotZ);
			}
		}
		return points;
	}

	public static Vector3f[] calculateCorners(Vector3f position, Vector3f dimensions, Vector3f toEdge, Vector3f rotation) {
		Vector3f corners[] = calculatePoints(dimensions.x, dimensions.y, dimensions.z, toEdge.x, toEdge.y, toEdge.z, rotation.x, rotation.y, rotation.z);
		for (int i = 0; i < corners.length; i++) {
			corners[i].x += position.x;
			corners[i].y += position.y;
			corners[i].z += position.z;
		}
		return corners;
	}

	public static Vector3f calculateIntersection(Vector3f left, Vector3f middle, Vector3f right, Vector3f ray1, Vector3f ray2) {
		Vector3f ml = Vector3f.sub(left, middle, null);
		Vector3f mr = Vector3f.sub(right, middle, null);
		Vector3f normal = Vector3f.cross(ml, mr, null);

		Vector3f ray = Vector3f.sub(ray2, ray1, null);

		float normalDotRay = Vector3f.dot(normal, ray);
		if (Math.abs(normalDotRay) < 1e-6f) {
			return null;
		}

		float factor = -Vector3f.dot(normal, Vector3f.sub(ray1, middle, null)) / normalDotRay;
		Vector3f intersection = Vector3f.add(ray1, new Vector3f(factor * ray.x, factor * ray.y, factor * ray.z), null);
		Vector3f middleIntersection = Vector3f.sub(intersection, middle, null);

		float u = Vector3f.dot(middleIntersection, ml);
		float v = Vector3f.dot(middleIntersection, mr);

		if(u >= 0.0f && u <= Vector3f.dot(ml, ml) && v >= 0.0f && v <= Vector3f.dot(mr, mr)){
			return intersection;
		}
		return null;
	}

	private static Vector3f rotateVector(Vector3f vector, float rotX, float rotY, float rotZ) {
		float x = vector.x;
		float y = vector.y;
		float z = vector.z;

		Vector2f rotation;
		if (z != 0 && x != 0) {
			rotation = rotate(z, x, rotY);
			z = rotation.x;
			x = rotation.y;
		}

		if (z != 0 && y != 0) {
			rotation = rotate(y, z, rotX);
			y = rotation.x;
			z = rotation.y;
		}

		if (y != 0 && x != 0) {
			rotation = rotate(y, x, rotZ);
			y = rotation.x;
			x = rotation.y;
		}

		return new Vector3f(x, y, z);
	}

	private static float cos(float rotDegree) {
		return (float) Math.cos(Math.toRadians(rotDegree));
	}

	private static float sin(float rotDegree) {
		return (float) Math.sin(Math.toRadians(rotDegree));
	}

	private static Vector2f rotate(float d1, float d2, float angle) {
		float s = (float) Math.sqrt(d1 * d1 + d2 * d2);
		if (d2 < 0) {
			angle += 180 - Math.toDegrees(Math.asin(d1 / s));
		} else {
			angle += Math.toDegrees(Math.asin(d1 / s));
		}
		return new Vector2f(s * sin(angle), s * cos(angle));
	}
}
