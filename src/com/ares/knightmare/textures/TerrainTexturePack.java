package com.ares.knightmare.textures;

public class TerrainTexturePack {

	private int backgroundTexture, rTexture, bTexture, gTexture;//TODO maybe delete this class as well

	public TerrainTexturePack(int backgroundTexture, int rTexture, int bTexture, int gTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.bTexture = bTexture;
		this.gTexture = gTexture;
	}

	public int getBackgroundTexture() {
		return backgroundTexture;
	}

	public int getrTexture() {
		return rTexture;
	}

	public int getbTexture() {
		return bTexture;
	}

	public int getgTexture() {
		return gTexture;
	}

}
