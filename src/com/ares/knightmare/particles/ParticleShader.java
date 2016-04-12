package com.ares.knightmare.particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.ares.knightmare.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/ares/knightmare/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/com/ares/knightmare/particles/particleFShader.txt";

	private int location_modelViewMatrix, location_projectionMatrix, location_texOffset1, location_texOffset2, location_texCoordInfo;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_texOffset1 = super.getUniformLocation("texOffset1");
		location_texOffset2 = super.getUniformLocation("texOffset2");
		location_texCoordInfo = super.getUniformLocation("texCoordInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numberOfRows, float blend){
		super.loadVector2f(location_texOffset1, offset1);
		super.loadVector2f(location_texOffset2, offset2);
		super.loadVector2f(location_texCoordInfo, new Vector2f(numberOfRows, blend));
	}

}
