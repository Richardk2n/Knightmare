package com.ares.knightmare.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ares.knightmare.loader.Loader;
import com.ares.knightmare.rendering.FontRenderer;
import com.ares.knightmare.util.fontMeshCreator.FontType;
import com.ares.knightmare.util.fontMeshCreator.GUIText;
import com.ares.knightmare.util.fontMeshCreator.TextMeshData;

public class TextMaster {

	private static Loader loader;
	private static HashMap<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(Loader loader){
		renderer = new FontRenderer();
		TextMaster.loader = loader;
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		loader.deleteVAO(text.getVAOID());
		if(textBatch.isEmpty()){
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
}
