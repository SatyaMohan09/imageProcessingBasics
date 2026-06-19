package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class BlurService {

    public String blur(String imagePath)
            throws IOException {

        BufferedImage image =
                ImageIO.read(new File(imagePath));

        int width = image.getWidth();
        int height = image.getHeight();

        ExtractPixels extractor =
                new ExtractPixels();

        int[] sourcePixels =
                extractor.extractPixels(image);

        int[] resultPixels =
                new int[width * height];

        // Copy original pixels so border pixels remain unchanged
        System.arraycopy(
                sourcePixels,
                0,
                resultPixels,
                0,
                sourcePixels.length
        );

        for (int y = 1; y < height - 1; y++) {

            for (int x = 1; x < width - 1; x++) {

                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;

                for (int ky = -1; ky <= 1; ky++) {

                    for (int kx = -1; kx <= 1; kx++) {

                        int nx = x + kx;
                        int ny = y + ky;

                        int neighborIndex =
                                ny * width + nx;

                        int rgb =
                                sourcePixels[neighborIndex];

                        int r =
                                (rgb >> 16) & 0xff;

                        int g =
                                (rgb >> 8) & 0xff;

                        int b =
                                rgb & 0xff;

                        redSum += r;
                        greenSum += g;
                        blueSum += b;
                    }
                }

                int r = redSum / 9;
                int g = greenSum / 9;
                int b = blueSum / 9;

                int targetIndex =
                        y * width + x;

                resultPixels[targetIndex] =
                        (r << 16) |
                        (g << 8) |
                        b;
            }
        }

        BufferedImage result =
                new BufferedImage(
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

        File original =
                new File(imagePath);

        String extension =
                getExtension(
                        original.getName()
                );

        String outputPath =
                original.getParent()
                        + File.separator
                        + "blur."
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