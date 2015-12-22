package com.ares.knightmare.terrain;

public class TerrainTexturePack {

	private TerrainTexture backgroundTexture, rTexture, bTexture, gTexture;

	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture bTexture, TerrainTexture gTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.bTexture = bTexture;
		this.gTexture = gTexture;
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

}
