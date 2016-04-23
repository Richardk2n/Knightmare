package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.textures.ParticleTexture;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float lifeLength, rotation, scale, elapsedTime = 0, distance, cw;//TODO use

	private ParticleTexture texture;

	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;

	public Particle(Vector3f position, Vector3f velocity, float cw, float lifeLength, float rotation, float scale, ParticleTexture texture) {
		this.position = position;
		this.velocity = velocity;
		this.cw = cw;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public boolean update(Camera camera) {
		velocity.y += Entity.GRAVITY;//TODO
		if(velocity.y<0){
			velocity.y += cw*velocity.y*velocity.y;
		}
		Vector3f.add(position, velocity, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoordInfo();
		elapsedTime++;
		return elapsedTime > lifeLength;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	public float getDistance(){
		return distance;
	}
}
