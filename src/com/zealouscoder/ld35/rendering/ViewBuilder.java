package com.zealouscoder.ld35.rendering;

public class ViewBuilder {

	private ViewBuilder realBuilder;

	public ViewBuilder() {
		
	}
	
	ViewBuilder copyView(GameView view) {
		realBuilder.copyView(view);
		return this;
	}
	
	public ViewBuilder(ViewBuilder realBuilder) {
		this.realBuilder = realBuilder;
	}
	
	public ViewBuilder size(double width, double height) {
		realBuilder.size(width, height);
		return this;
	}
	
	public ViewBuilder start(double x, double y) {
		realBuilder.start(x, y);
		return this;
	}
	
	public ViewBuilder scale(double x, double y) {
		realBuilder.scale(x, y);
		return this;
	}
	
	public ViewBuilder rotation(double rot) {
		realBuilder.rotation(rot);
		return this;
	}
	
	public GameView build() {
		return realBuilder.build();
	}

	public ViewBuilder upALayer() {
		realBuilder.upALayer();
		return this;
	}
	
}
