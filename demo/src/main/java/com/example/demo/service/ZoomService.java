package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ZoomService {

    public String zoom(
            String imagePath,
            double factor)
            throws IOException {

        if (factor <= 0) {
            throw new IllegalArgumentException(
                    "Factor must be greater than zero"
            );
        }

        BufferedImage image =
                ImageIO.read(new File(imagePath));

        int originalWidth =
                image.getWidth();

        int originalHeight =
                image.getHeight();

        int newWidth =
                (int) (originalWidth * factor);

        int newHeight =
                (int) (originalHeight * factor);

        ExtractPixels extractor =
                new ExtractPixels();

        int[] sourcePixels =
                extractor.extractPixels(image);

        int[] resultPixels =
                new int[newWidth * newHeight];

        for (int y = 0; y < newHeight; y++) {

            for (int x = 0; x < newWidth; x++) {

                int sourceX =
                        (int) (x / factor);

                int sourceY =
                        (int) (y / factor);

                sourceX = Math.min(
                        sourceX,
                        originalWidth - 1
                );

                sourceY = Math.min(
                        sourceY,
                        originalHeight - 1
                );

                int sourceIndex =
                        sourceY * originalWidth + sourceX;

                int targetIndex =
                        y * newWidth + x;

                resultPixels[targetIndex] =
                        sourcePixels[sourceIndex];
            }
        }

        BufferedImage result =
                new BufferedImage(
                        newWidth,
                        newHeight,
                        image.getType()
                );

        result.setRGB(
                0,
                0,
                newWidth,
                newHeight,
                resultPixels,
                0,
                newWidth
        );

        File original =
                new File(imagePath);

        String extension =
                getExtension(
                        original.getName()
                );

        String outputPath =
                original.getParent()
                        + File.separator
                        + "zoom_" + factor + "."
                        + extension;

        ImageIO.write(
                result,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }

    private String getExtension(
            String fileName) {

        int dot =
                fileName.lastIndexOf('.');

        return fileName.substring(dot + 1);
    }
}