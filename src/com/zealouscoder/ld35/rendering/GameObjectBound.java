package com.zealouscoder.ld35.rendering;

public class GameObjectBound {

	private double height;
	private double width;

	public GameObjectBound(double width, double height) {
		this.width = width;
		this.height= height;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getRadiusSq() {
		double halfW = width/2;
		double halfH = height/2;
		return halfW*halfW + halfH*halfH;
	}
	
}
