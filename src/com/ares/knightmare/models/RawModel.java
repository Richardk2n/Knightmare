package com.ares.knightmare.models;

public class RawModel {

	private int vaoID, vertexCount;
	private float height, width, depth, toTop, toRight, toEnd;
	
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public float getToTop() {
		return toTop;
	}

	public void setToTop(float toTop) {
		this.toTop = toTop;
	}

	public float getToRight() {
		return toRight;
	}

	public void setToRight(float toRight) {
		this.toRight = toRight;
	}

	public float getToEnd() {
		return toEnd;
	}

	public void setToEnd(float toEnd) {
		this.toEnd = toEnd;
	}
}
