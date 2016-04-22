package com.ares.knightmare.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.util.Maths;

public class EntityShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private static final String VERTEX_FILE = "/shaders/vertexShaderEntity", FRAGMENT_FILE = "/shaders/fragmentShaderEntity";

	private int location_transformationMatrix, location_viewMatrix, location_projectionMatrix, location_lightPosition[], location_lightColor[], location_shineDamper,
			location_reflectivity, location_useFakeLighting, location_skyColor, location_numberOfRows, location_offset, location_attenuation[], location_plane;

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDaper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColor = super.getUniformLocation("skyColor");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation(new StringBuilder("lightPosition[").append(i).append("]").toString());
			location_lightColor[i] = super.getUniformLocation(new StringBuilder("lightColor[").append(i).append("]").toString());
			location_attenuation[i] = super.getUniformLocation(new StringBuilder("attenuation[").append(i).append("]").toString());
		}
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadProjctionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(location_lightPosition[i],lights.get(i).getPosition());
				super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
				super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(location_useFakeLighting, useFake);
	}

	public void loadSkyColor(float r, float g, float b) {
		super.loadVector3f(location_skyColor, new Vector3f(r, g, b));
	}

	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	public void loadOffset(float x, float y) {
		super.loadVector2f(location_offset, new Vector2f(x, y));
	}
	
	public void loadClipPlane(Vector4f plane){
		super.loadVector4f(location_plane, plane);
	}
}
