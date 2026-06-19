package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Component {

    private List<Point> pixels = new ArrayList<>();

    public Component() {
    }

    public List<Point> getPixels() {
        return pixels;
    }

    public void setPixels(List<Point> pixels) {
        this.pixels = pixels;
    }

    public void addPixel(Point point) {
        this.pixels.add(point);
    }

    public int getArea() {
        return pixels.size();
    }
}