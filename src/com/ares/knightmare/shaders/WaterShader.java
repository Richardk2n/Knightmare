package com.ares.knightmare.shaders;
 
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.util.Maths;
 
public class WaterShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;
 
    private final static String VERTEX_FILE = "res/shaders/vertexShaderWater";
    private final static String FRAGMENT_FILE = "res/shaders/fragmentShaderWater";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_normalMap;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_attenuation;
	private int location_depthMap;
	private int location_near;
	private int location_far;
    
    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
        location_normalMap = getUniformLocation("normalMap");
		location_shineDamper = getUniformLocation("shineDaper");
		location_reflectivity = getUniformLocation("reflectivity");
		location_skyColor = getUniformLocation("skyColor");
		location_depthMap = getUniformLocation("depthMap");
		location_near = getUniformLocation("near");
		location_far = getUniformLocation("far");
		
        location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = getUniformLocation(new StringBuilder("lightPosition[").append(i).append("]").toString());
			location_lightColor[i] = getUniformLocation(new StringBuilder("lightColor[").append(i).append("]").toString());
			location_attenuation[i] = getUniformLocation(new StringBuilder("attenuation[").append(i).append("]").toString());
		}
    }
 
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    	super.loadVector3f(location_cameraPosition, camera.getPosition());
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
    
    public void connectTextureUnits(){
    	super.loadInt(location_reflectionTexture, 0);
    	super.loadInt(location_refractionTexture, 1);
    	super.loadInt(location_dudvMap, 2);
    	super.loadInt(location_normalMap, 3);
    	super.loadInt(location_depthMap, 4);
    }
    
    public void loadMoveFactor(float moveFactor){
    	super.loadFloat(location_moveFactor, moveFactor);
    }

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
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

	public void loadSkyColor(float r, float g, float b) {
		super.loadVector3f(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadPlanes(float near, float far){
		super.loadFloat(location_near, near);
		super.loadFloat(location_far, far);
	}
 
}