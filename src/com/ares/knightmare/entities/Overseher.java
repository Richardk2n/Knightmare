package com.ares.knightmare.entities;

public class Overseher extends Camera{

	public Overseher(int width, int height) {
		super(width, height);
		rotX = 90;
	}
	
	public void rotate(float rotX, float rotY, float rotZ) {
		move(rotY, 0, rotX);//TODO xy
	}
	
	public void move(float ad, float ss, float ws) {
		position.x += ad;// TODO both
		position.y += ss;
		position.z -= ws;
		if(position.y<=0){
			position.y=0.2f;
		}
	}

}
