package com.ares.knightmare.particles;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.handler.ParticleHandler;

public class ParticleSystem {

	private float ppt;
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	private ParticleTexture particleTexture;
	private ParticleHandler particleHandler;

	public ParticleSystem(float ppt, float speed, float gravityComplient, float lifeLength, ParticleTexture particleTexture) {
		this.ppt = ppt;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.particleTexture = particleTexture;
	}

	public void tick(Vector3f systemCenter) {
		int count = (int) Math.floor(ppt);
		float partialParticle = ppt % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}
	
	public void setHandler(ParticleHandler handler){
		particleHandler = handler;
	}

	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 1f - 1f;//TODO 2f
		float dirZ = (float) Math.random() * 1f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		particleHandler.store(new Particle(new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 1, particleTexture));
	}

}
