package com.zealouscoder.ld35.rendering;

import com.zealouscoder.ld35.movement.GamePosition;

public class GameObjectBound {

    private double height;
    private double width;

    public GameObjectBound(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getRadiusSq() {
        double halfW = width / 2;
        double halfH = height / 2;
        return halfW * halfW + halfH * halfH;
    }

    public boolean collides(GamePosition thisPos, GameObjectBound bounds, GamePosition otherPos) {
        return inside(thisPos, otherPos.getX(), otherPos.getY())
                || inside(thisPos, otherPos.getX() + bounds.width, otherPos.getY())
                || inside(thisPos, otherPos.getX() + bounds.width, otherPos.getY() + bounds.height)
                || inside(thisPos, otherPos.getX(), otherPos.getY() + bounds.height)
                || bounds.inside(otherPos, thisPos.getX(), thisPos.getY())
                || bounds.inside(otherPos, thisPos.getX()+getWidth(), thisPos.getY())
                || bounds.inside(otherPos, thisPos.getX(), thisPos.getY()+getHeight())
                || bounds.inside(otherPos, thisPos.getX()+getWidth(), thisPos.getY()+getHeight());
    }

    public boolean inside(GamePosition thisPos, double x, double y) {
        return (x <= thisPos.getX() + getWidth() && x >= thisPos.getX())
                && (y <= thisPos.getY() + getHeight() && y >= thisPos.getY());
    }

}
