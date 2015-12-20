package com.ares.knightmare.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.shaders.StaticShader;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	public MasterRenderer(int width, int height){
		renderer = new Renderer(shader, width, height);
	}
	
	public void render(Light sun, Camera camera){
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear(); //TODO change up why per frame??
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch==null){
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}else{
			batch.add(entity);
		}
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
