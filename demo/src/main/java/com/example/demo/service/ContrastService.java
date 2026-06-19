package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ContrastService {

    public String adjustContrast(
            String imagePath,
            double factor) throws IOException {

        BufferedImage image =
                ImageIO.read(new File(imagePath));

        int width = image.getWidth();
        int height = image.getHeight();

        ExtractPixels extractor = new ExtractPixels();
        int[] pixels = extractor.extractPixels(image);

        for (int i = 0; i < pixels.length; i++) {

            int rgb = pixels[i];

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            r = clamp(
                    (int) (factor * (r - 128) + 128)
            );

            g = clamp(
                    (int) (factor * (g - 128) + 128)
            );

            b = clamp(
                    (int) (factor * (b - 128) + 128)
            );

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

        String extension =
                getExtension(original.getName());

        String outputPath =
                original.getParent()
                        + File.separator
                        + "contrast."
                        + extension;

        ImageIO.write(
                result,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }

    private int clamp(int value) {

        return Math.max(
                0,
                Math.min(255, value)
        );
    }

    private String getExtension(String fileName) {

        int dotIndex =
                fileName.lastIndexOf('.');

        return fileName.substring(dotIndex + 1);
    }
}