package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Light;
import com.ares.knightmare.util.Maths;

public class LightHandler {

	private static final float CHUNCK_SIZE = 100;
	private static final int RENDER_DISTANCE = 8;
	private List<Light> lights = new ArrayList<>();

	private Light sun;
	private boolean calculateSun = true;

	public void store(Light light) {
		lights.add(light);
	}

	public void setSun(Light sun) {
		this.sun = sun;
	}

	public void setSunActive(boolean sunActive) {
		calculateSun = sunActive;
	}

	public void remove(Light light) {
		lights.remove(light);
	}

	public List<Light> getNearestLights(Vector3f position) {
		int x = (int) Math.floor(position.x / CHUNCK_SIZE);
		int y = (int) Math.floor(position.y / CHUNCK_SIZE);
		int z = (int) Math.floor(position.z / CHUNCK_SIZE);

		List<Light> lights = new ArrayList<>();
		for (Light light : this.lights) {
			Vector3f positionE = light.getPosition();
			int xL = (int) Math.floor(positionE.x / CHUNCK_SIZE);
			int yL = (int) Math.floor(positionE.y / CHUNCK_SIZE);
			int zL = (int) Math.floor(positionE.z / CHUNCK_SIZE);

			if (x - RENDER_DISTANCE <= xL && xL <= x + RENDER_DISTANCE && y - RENDER_DISTANCE <= yL && yL <= y + RENDER_DISTANCE && z - RENDER_DISTANCE <= zL
					&& zL <= z + RENDER_DISTANCE) {
				lights.add(light);
			}
		}
		return sort(lights, position);
	}
	
	public List<Light> sort(List<Light> lights, Vector3f position){
		List<Light> sortetLights = new ArrayList<>();
		if(lights.size()>0){
			sortetLights.add(lights.get(0));
		}
		for (int j = 1; j<lights.size(); j++) {
			Light light = lights.get(j);
			float distance1 = Math.abs(Maths.calculateDistance(position, light.getPosition()));
			for (int i = 0; i < sortetLights.size(); i++) {
				float distance2 = Math.abs(Maths.calculateDistance(position, sortetLights.get(i).getPosition()));

				if (distance1 < distance2) {
					sortetLights.add(i, light);
					break;
				} else if (i == sortetLights.size() - 1) {
					sortetLights.add(light);
					break;
				}
			}
		}
		if (calculateSun && sun != null) {
			sortetLights.add(0, sun);
		}
		return sortetLights;
	}

	public Light getSun() {
		return sun;
	}
}