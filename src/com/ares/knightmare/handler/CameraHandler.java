package com.ares.knightmare.handler;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;

public class CameraHandler {

	private Camera overseher;
	private Entity entity;
	
	public CameraHandler(Camera overseher){
		this.overseher = overseher;
	}
	
	public void bindEntity(Entity entity){
		this.entity = entity;
		if(entity!=null && !entity.hasCamera()){
			entity.setCamera(new Camera(getWidth(), getHeight()));
		}
	}
	
	public void rotate(float rotX, float rotY, float rotZ){
		if(entity==null){
			overseher.rotate(rotX, rotY, rotZ);
		}else{
			entity.rotate(rotX, rotY, rotZ);
		}
	}
	
	public void move(float ad, float ss, float ws){
		if(entity==null){
			overseher.move(ad, ss, ws);
		}else{
			entity.move(ad, ss, -ws);
		}
	}
	
	public void applyAcceleration(float ad, float ss, float ws){
		entity.applyAcceleration(ad, ss, ws);
	}
	
	public void zoom(double amount){
		if(entity==null){
			overseher.move(0, -Float.parseFloat(String.valueOf(amount)), 0);
		}else{
			entity.zoom(Float.parseFloat(String.valueOf(amount)));
		}
	}
	
	public Camera getCamera(){
		if(entity==null){
			return overseher;
		}else{
			return entity.getCamera();
		}
	}
	
	public boolean isBound(){
		return entity!=null;
	}
	
	public int getWidth(){
		return overseher.getWidth();
	}
	
	public int getHeight(){
		return overseher.getHeight();
	}
}
