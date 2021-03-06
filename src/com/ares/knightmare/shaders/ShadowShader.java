package com.ares.knightmare.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/shaders/ShadowVertexShader.txt", FRAGMENT_FILE = "/shaders/ShadowFragmentShader.txt";
	
	private int location_mvpMatrix, location_numberOfRows, location_offset;

	public ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		
	}
	
	public void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	public void loadOffset(float x, float y) {
		super.loadVector2f(location_offset, new Vector2f(x, y));
	}

}
