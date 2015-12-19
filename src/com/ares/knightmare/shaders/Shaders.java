package com.ares.knightmare.shaders;

public class Shaders {

	public static ShaderProgram staticShader;
	
	public static void create(){
		staticShader = new StaticShader();
	}
	
	public static void cleanUp(){
		staticShader.cleanUp();
	}
}
