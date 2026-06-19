package com.example.demo.model;

public class DetectedShape {

    private String shape;
    private int area;
    private int width;
    private int height;

    public DetectedShape() {
    }

    public DetectedShape(
            String shape,
            int area,
            int width,
            int height) {

        this.shape = shape;
        this.area = area;
        this.width = width;
        this.height = height;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}