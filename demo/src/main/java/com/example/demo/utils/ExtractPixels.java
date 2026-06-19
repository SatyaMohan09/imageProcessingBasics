package com.example.demo.utils;

import java.awt.image.BufferedImage;

public class ExtractPixels {
    public int[] extractPixels(BufferedImage image) {

    int width = image.getWidth();
    int height = image.getHeight();

    int[] pixels = new int[width * height];

    image.getRGB(
            0,
            0,
            width,
            height,
            pixels,
            0,
            width
    );

    return pixels;
}
}
