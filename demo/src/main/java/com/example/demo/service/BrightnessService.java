package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class BrightnessService {

    public String adjustBrightness(
            String imagePath,
            int brightnessValue) throws IOException {

        BufferedImage image = ImageIO.read(new File(imagePath));

        int width = image.getWidth();
        int height = image.getHeight();

        ExtractPixels extractor = new ExtractPixels();

        int[] pixels = extractor.extractPixels(image);

        for (int i = 0; i < pixels.length; i++) {

            int rgb = pixels[i];

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            r = clamp(r + brightnessValue);
            g = clamp(g + brightnessValue);
            b = clamp(b + brightnessValue);

            pixels[i] =
                    (r << 16) |
                    (g << 8) |
                    b;
        }

        BufferedImage result =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        result.setRGB(
                0,
                0,
                width,
                height,
                pixels,
                0,
                width
        );

        File original = new File(imagePath);

        String fileName = original.getName();

        int dotIndex = fileName.lastIndexOf('.');

        String extension = fileName.substring(dotIndex + 1);

        String outputPath = original.getParent()
                        + File.separator
                        + "brightness."
                        + extension;

        ImageIO.write(
                result,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }

    private int clamp(int value) {

        if (value < 0) {
            return 0;
        }

        if (value > 255) {
            return 255;
        }

        return value;
    }
}