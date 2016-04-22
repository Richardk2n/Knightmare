package com.ares.knightmare.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/ParticleVertexShader.txt", FRAGMENT_FILE = "/shaders/particleFragmentShader.txt";

	private int location_projectionMatrix, location_numberOfRows;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffset");
		super.bindAttribute(6, "blendFactor");
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadNumberOfRows(float numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

}
