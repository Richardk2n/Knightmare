package com.ares.knightmare.postProcessing;

import com.ares.knightmare.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/ares/knightmare/postProcessing/contrastVertex.txt";
	private static final String FRAGMENT_FILE = "/com/ares/knightmare/postProcessing/contrastFragment.txt";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
