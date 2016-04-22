package com.ares.knightmare.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/shaders/FontVertexShader.txt", FRAGMENT_FILE = "/shaders/FontFragmentShader.txt";
	
	private int location_translation, location_color;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_translation = super.getUniformLocation("translation");
		location_color = super.getUniformLocation("color");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	public void loadColor(Vector3f color){
		super.loadVector3f(location_color, color);
	}
	
	public void loadTranslation(Vector2f translation){
		super.loadVector2f(location_translation, translation);
	}

}
