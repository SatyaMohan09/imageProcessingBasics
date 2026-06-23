package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class GrayscaleService {

    public String convertToGrayscale(String imagePath)
            throws IOException {

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

            int gray =(int) (
                            0.299 * r +
                            0.587 * g +
                            0.114 * b
                    );

            pixels[i] = (gray << 16) | (gray << 8) | gray;
        }

        BufferedImage grayImage =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        grayImage.setRGB(
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
                        + "grayscale."
                        + extension;

        ImageIO.write(
                grayImage,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }
}