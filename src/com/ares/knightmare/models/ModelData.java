package com.ares.knightmare.models;

public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint, height, width, depth, toTop, toRight, toEnd;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public float getFurthestPoint() {
		return furthestPoint;
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
