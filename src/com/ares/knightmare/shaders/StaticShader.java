package com.ares.knightmare.shaders;

import org.lwjgl.util.vector.Matrix4f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.util.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/vertexShaderTexture", FRAGMENT_FILE = "res/shaders/fragmentShaderTexture";

	private int location_transformationMatrix, location_viewMatrix, location_projectionMatrix, location_lightPosition, location_lightColor, location_shineDamper, location_reflectivity;

	public StaticShader() {
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
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDaper");
		location_reflectivity = super.getUniformLocation("reflectivity");
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

	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getClolor());
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
}
