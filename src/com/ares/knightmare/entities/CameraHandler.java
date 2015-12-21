package com.ares.knightmare.entities;

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
	
	public void rotate(float xz, float yz, float xy){
		if(entity==null){
			overseher.rotate(xz, yz, xy);
		}else{
			entity.rotate(xz, yz, xy);
		}
	}
	
	public void move(float ad, float ss, float ws){
		if(entity==null){
			overseher.move(ad, ss, ws);
		}else{
			entity.move(ad, ss, -ws);
		}
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
