package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class WaterTile {
     
    public static final float TILE_SIZE = 50;
	private static final float WAVE_SPEED = 0.0003f;
     
    private float height;
    private float x,z, moveFactor = 0;
     
    public WaterTile(float centerX, float centerZ, float height){
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
    }
 
    public float getHeight() {
        return height;
    }
 
    public float getX() {
        return x;
    }
 
    public float getZ() {
        return z;
    }
 
    public Vector3f getPosition(){
    	return new Vector3f(x, height, z);
    }
    
    public void tick(){
    	setMoveFactor(getMoveFactor() + WAVE_SPEED);
    	moveFactor %= 1;
    }

	public float getMoveFactor() {
		return moveFactor;
	}

	public void setMoveFactor(float moveFactor) {
		this.moveFactor = moveFactor;
	}
	
	public Vector3f getCentralPosition(){
		return new Vector3f(x+0.5f*TILE_SIZE, 0, z+0.5f*TILE_SIZE);
	}
 
}