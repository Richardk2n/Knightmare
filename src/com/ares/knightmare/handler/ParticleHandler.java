package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Particle;
import com.ares.knightmare.entities.ParticleSystem;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.textures.ParticleTexture;
import com.ares.knightmare.util.InsertionSort;

public class ParticleHandler extends Semaphore{

	public ParticleHandler(int permits) {
		super(permits);
	}

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private List<Particle> particles = new ArrayList<>();
	private List<ParticleSystem> particleSystems = new ArrayList<>();
//	private Map<ParticleTexture, List<Particle>> particlesTexture = new HashMap<>();TODO

	public void store(Particle particle) {
		particles.add(particle);
	}

	public void remove(Particle particle) {
		particles.remove(particle);
	}
	
	public void store(ParticleSystem particleSystem) {
		particleSystem.setHandler(this);
		particleSystems.add(particleSystem);
	}

	public void remove(ParticleSystem particleSystem) {
		particleSystems.remove(particleSystem);
	}

	public Map<ParticleTexture, List<Particle>> getRenderedParticles(Camera camera) {
		Vector3f position = camera.getPosition(); // TODO view direction
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);

		List<Particle> particles = new ArrayList<>();
		for (Particle particle : this.particles) {
			int xE = (int) Math.floor(particle.getPosition().x/CHUNCK_SIZE);
			int yE = (int) Math.floor(particle.getPosition().y/CHUNCK_SIZE);
			int zE = (int) Math.floor(particle.getPosition().z/CHUNCK_SIZE);

			if (x - RENDER_DISTANCE <= xE && xE <= x + RENDER_DISTANCE && y - RENDER_DISTANCE <= yE && yE <= y + RENDER_DISTANCE && z - RENDER_DISTANCE <= zE
					&& zE <= z + RENDER_DISTANCE) {
				particles.add(particle);
			}
		}
		return sort(particles);
	}

	private Map<ParticleTexture, List<Particle>> sort(List<Particle> particles) {
		Map<ParticleTexture, List<Particle>> map = new HashMap<>();

		for (Particle particle : particles) {
			ParticleTexture particleTexture = particle.getTexture();
			List<Particle> batch = map.get(particleTexture);
			if (batch == null) {
				List<Particle> newBatch = new ArrayList<>();
				newBatch.add(particle);
				map.put(particleTexture, newBatch);
			} else {
				batch.add(particle);
			}
		}
		return map;
	}
	
	public void tick(Camera camera) {
		for(ParticleSystem particleSystem: particleSystems){
			particleSystem.tick(new Vector3f(0, 5, 0));//TODO
		}
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()){
			Particle p = iterator.next();
			if(p.update(camera)){
				iterator.remove();
			}
		}
		InsertionSort.sortHighToLow(particles);
	}
}
