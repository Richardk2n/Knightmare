package com.ares.knightmare.entities;

public class Overseher extends Camera{

	public Overseher(int width, int height) {
		super(width, height);
		yz = 90;
	}
	
	public void rotate(float xz, float yz, float xy) {
		move(xz, 0, yz);//TODO xy
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
