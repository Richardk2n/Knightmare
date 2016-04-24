package com.ares.knightmare.entities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.models.Model;
import com.ares.knightmare.textures.TerrainTexture;
import com.ares.knightmare.textures.TerrainTexturePack;
import com.ares.knightmare.util.HeightsGenerator;
import com.ares.knightmare.util.Maths;

public class Terrain {

	private static final float SIZE = 100, MAX_HEIGHT = 5, MIN_HEIGHT = -5, MAX_PIXEL_COLOR = 256 * 256 * 256;//TODO SIZE chunksize

	private float x, z, heights[][], height;
	private Model model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[] vertices, normals, textureCoords;
	private int[] indices;

	public Terrain(int gridX, int gridZ, float height, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		x = gridX * SIZE;
		z = gridZ * SIZE;
		this.height = height;
		model = generateTerrain(loader, heightMap);
	}
	
	public Terrain(int gridX, int gridZ, float height, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		x = gridX * SIZE;
		z = gridZ * SIZE;
		this.height = height;
		model = generateTerrainUsingGenerator(loader);
	}

	private Model generateTerrain(Loader loader, String heightMap) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Class.class.getResourceAsStream(new StringBuilder("/maps/height/").append(heightMap).append(".png").toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textureCoords = new float[count * 2];
		indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		for(int i = 0; i < VERTEX_COUNT; i++){
			vertexPointer = i*VERTEX_COUNT;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int i = 0; i < VERTEX_COUNT; i++){
			vertexPointer = i*VERTEX_COUNT + VERTEX_COUNT-1;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int j = 0; j < VERTEX_COUNT; j++){
			vertexPointer = j;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int j = 0; j < VERTEX_COUNT; j++){
			vertexPointer = (VERTEX_COUNT-1)*VERTEX_COUNT+j;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Model generateTerrainUsingGenerator(Loader loader) {
		
		HeightsGenerator generator = new HeightsGenerator((int)Math.floor(x/SIZE), (int)Math.floor(z/SIZE), 256, 123456789);
		
		int VERTEX_COUNT = 256;//TODO
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textureCoords = new float[count * 2];
		indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		for(int i = 0; i < VERTEX_COUNT; i++){
			vertexPointer = i*VERTEX_COUNT;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int i = 0; i < VERTEX_COUNT; i++){
			vertexPointer = i*VERTEX_COUNT + VERTEX_COUNT-1;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int j = 0; j < VERTEX_COUNT; j++){
			vertexPointer = j;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		for(int j = 0; j < VERTEX_COUNT; j++){
			vertexPointer = (VERTEX_COUNT-1)*VERTEX_COUNT+j;
			normals[vertexPointer * 3] = 0;
			normals[vertexPointer * 3 + 1] = 1;
			normals[vertexPointer * 3 + 2] = 0;
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		height = Math.max(height, MIN_HEIGHT);
		return height + this.height;
	}

	private Vector3f calculateNormal(int x, int z, HeightsGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, HeightsGenerator generator) {
		return generator.generateHeight(x, z);
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - x;
		float terrainZ = worldZ - z;

		float gridSquareSize = SIZE / ((float) heights.length - 1);

		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1) {
			gridX--;
		}
		if (gridZ >= heights.length - 1) {
			gridZ--;
		}
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
					new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public float getHeight() {
		return height;
	}

	public Model getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public Vector3f getCentralPosition() {
		return new Vector3f((x + SIZE) / 2, (MIN_HEIGHT + MAX_HEIGHT) / 2, (z + SIZE) / 2);
	}
	
	public void alterTerrain(int x, int z, float height, Loader loader){
		int vertexPointer = z*heights.length+x;
		heights[x][z] = height;
		vertices[vertexPointer * 3 + 1] = height;
//		Vector3f normal = calculateNormal(x, z, image);TODO
//		normals[vertexPointer * 3] = normal.x;
//		normals[vertexPointer * 3 + 1] = normal.y;
//		normals[vertexPointer * 3 + 2] = normal.z;
		loader.deleteVAO(model.getVaoID());
		model = loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

}
