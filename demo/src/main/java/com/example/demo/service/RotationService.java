package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import com.example.demo.utils.ExtractPixels;

@Service
public class RotationService {

    public String rotate(String imagePath, int angle)
            throws IOException {

        BufferedImage image = ImageIO.read(new File(imagePath));

        BufferedImage result;

        switch (angle) {

            case 90:
                result = rotate90(image);
                break;

            case 180:
                result = rotate180(image);
                break;

            case 270:
                result = rotate270(image);
                break;

            default:
                throw new IllegalArgumentException(
                        "Supported angles: 90, 180, 270"
                );
        }

        File original = new File(imagePath);

        String extension =
                getExtension(original.getName());

        String outputPath =
                original.getParent()
                        + File.separator
                        + "rotation_" + angle + "."
                        + extension;

        ImageIO.write(
                result,
                extension,
                new File(outputPath)
        );

        return outputPath;
    }

private BufferedImage rotate90(
        BufferedImage image) {

    int width = image.getWidth();
    int height = image.getHeight();

    ExtractPixels extractor = new ExtractPixels();

    int[] sourcePixels =
            extractor.extractPixels(image);

    int[] resultPixels =
            new int[width * height];

    for (int y = 0; y < height; y++) {

        for (int x = 0; x < width; x++) {

            int sourceIndex =
                    y * width + x;

            int newX =
                    height - 1 - y;

            int newY =
                    x;

            int targetIndex =
                    newY * height + newX;

            resultPixels[targetIndex] =
                    sourcePixels[sourceIndex];
        }
    }

    BufferedImage result =
            new BufferedImage(
                    height,
                    width,
                    image.getType()
            );

    result.setRGB(
            0,
            0,
            height,
            width,
            resultPixels,
            0,
            height
    );

    return result;
}

private BufferedImage rotate180(
        BufferedImage image) {

    int width = image.getWidth();
    int height = image.getHeight();

    ExtractPixels extractor = new ExtractPixels();

    int[] sourcePixels = extractor.extractPixels(image);

    int[] resultPixels = new int[width * height];

    for (int y = 0; y < height; y++) {

        for (int x = 0; x < width; x++) {

            int sourceIndex = y * width + x;

            int newX = width - 1 - x;

            int newY = height - 1 - y;

            int targetIndex =  newY * width + newX;

            resultPixels[targetIndex] =
                    sourcePixels[sourceIndex];
        }
    }

    BufferedImage result = new BufferedImage(
                    width,
                    height,
                    image.getType()
            );

    result.setRGB(
            0,
            0,
            width,
            height,
            resultPixels,
            0,
            width
    );

    return result;
}

private BufferedImage rotate270(
        BufferedImage image) {

    int width = image.getWidth();
    int height = image.getHeight();

    ExtractPixels extractor = new ExtractPixels();

    int[] sourcePixels = extractor.extractPixels(image);

    int[] resultPixels = new int[width * height];

    for (int y = 0; y < height; y++) {

        for (int x = 0; x < width; x++) {

            int sourceIndex = y * width + x;

            int newX = y;

            int newY = width - 1 - x;

            int targetIndex = newY * height + newX;

            resultPixels[targetIndex] =
                    sourcePixels[sourceIndex];
        }
    }

    BufferedImage result = new BufferedImage(
                    height,
                    width,
                    image.getType()
            );

    result.setRGB(
            0,
            0,
            height,
            width,
            resultPixels,
            0,
            height
    );

    return result;
}

    private String getExtension(
            String fileName) {

        int dot = fileName.lastIndexOf('.');

        return fileName.substring(dot + 1);
    }
}