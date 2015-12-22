package com.ares.knightmare.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position, clolor, attenuation = new Vector3f(1, 0, 0);

	public Light(Vector3f position, Vector3f clolor) {
		this.position = position;
		this.clolor = clolor;
	}
	
	public Light(Vector3f position, Vector3f clolor, Vector3f attenuation) {
		this.position = position;
		this.clolor = clolor;
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

	public Vector3f getClolor() {
		return clolor;
	}

	public void setClolor(Vector3f clolor) {
		this.clolor = clolor;
	}

}
