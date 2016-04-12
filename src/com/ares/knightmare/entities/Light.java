package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position, color, attenuation = new Vector3f(1, 0, 0);

	public Light(Vector3f position, Vector3f clolor) {
		this.position = position;
		this.color = clolor;
	}
	
	public Light(Vector3f position, Vector3f clolor, Vector3f attenuation) {
		this.position = position;
		this.color = clolor;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation(){
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

}
