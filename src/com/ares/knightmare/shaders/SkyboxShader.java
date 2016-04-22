package com.ares.knightmare.shaders;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.util.Maths;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/SkyboxVertexShader", FRAGMENT_FILE = "/shaders/SkyboxFragmentShader";
	private static final float ROTATE_SPEED = 0.01f;
	private static final int TIME_SPEED = 10;

	private int location_projectionMatrix, location_viewMatrix, location_fogColor, location_cubeMap, location_cubeMap2, location_blendFactor, currentTime;
	private float currenRotation;
	private Timer timer;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void start() {
		super.start();
		if (timer == null) {
			timer = new Timer(true);
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					currenRotation += ROTATE_SPEED;
					currentTime += TIME_SPEED;
				}
			}, 0, 10);
		}
	}
	
	public int getTime(){
		return currentTime;
	}

	public void cleanUp() {
		super.cleanUp();
		if (timer != null) {
			timer.cancel();
		}
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		Matrix4f.rotate((float) Math.toRadians(currenRotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void loadFogColor(float r, float g, float b) {
		super.loadVector3f(location_fogColor, new Vector3f(r, g, b));
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	public void loadBlendFactor(float blend){
		super.loadFloat(location_blendFactor, blend);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
