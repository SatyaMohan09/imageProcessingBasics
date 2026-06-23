package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class HorizontalFlipService {

    public String flip(String imagePath)
            throws IOException {

        BufferedImage image = ImageIO.read(new File(imagePath));

        int width = image.getWidth();
        int height = image.getHeight();

        ExtractPixels extractor = new ExtractPixels();

        int[] sourcePixels = extractor.extractPixels(image);

        int[] resultPixels = new int[width * height];

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int sourceIndex = y * width + x;

                int targetIndex = y * width + (width - 1 - x);

                resultPixels[targetIndex] = sourcePixels[sourceIndex];
            }
        }

        BufferedImage result = new BufferedImage(width, height, image.getType());

        result.setRGB(
                0,
                0,
                width,
                height,
                resultPixels,
                0,
                width
        );

        File original = new File(imagePath);

        String extension = getExtension(original.getName());

        String outputPath = original.getParent()
                        + File.separator
                        + "horizontal_flip."
                        + extension;

        ImageIO.write(
                result,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }

    private String getExtension(String fileName) {

        int dot = fileName.lastIndexOf('.');

        return fileName.substring(dot + 1);
    }
}