package com.example.demo.model;

public class BoundingBox {

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public BoundingBox() {
    }

    public BoundingBox(
            int minX,
            int maxX,
            int minY,
            int maxY) {

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getWidth() {
        return maxX - minX + 1;
    }

    public int getHeight() {
        return maxY - minY + 1;
    }
}