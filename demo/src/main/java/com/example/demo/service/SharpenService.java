package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class SharpenService {

    public void sharpen(String imagePath)
            throws IOException {

        BufferedImage original =
                ImageIO.read(new File(imagePath));

        int width =
                original.getWidth();

        int height =
                original.getHeight();

        ExtractPixels extractor =
                new ExtractPixels();

        int[] sourcePixels =
                extractor.extractPixels(original);

        int[] resultPixels =
                new int[width * height];

        // Preserve border pixels
        System.arraycopy(
                sourcePixels,
                0,
                resultPixels,
                0,
                sourcePixels.length
        );

        int[][] kernel = {
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        };

        for (int y = 1; y < height - 1; y++) {

            for (int x = 1; x < width - 1; x++) {

                int red = 0;
                int green = 0;
                int blue = 0;

                for (int ky = -1; ky <= 1; ky++) {

                    for (int kx = -1; kx <= 1; kx++) {

                        int nx =
                                x + kx;

                        int ny =
                                y + ky;

                        int neighborIndex =
                                ny * width + nx;

                        int rgb =
                                sourcePixels[neighborIndex];

                        int weight =
                                kernel[ky + 1][kx + 1];

                        red +=
                                ((rgb >> 16) & 0xff)
                                        * weight;

                        green +=
                                ((rgb >> 8) & 0xff)
                                        * weight;

                        blue +=
                                (rgb & 0xff)
                                        * weight;
                    }
                }

                red = clamp(red);
                green = clamp(green);
                blue = clamp(blue);

                int newRgb =
                        (red << 16)
                                | (green << 8)
                                | blue;

                int targetIndex =
                        y * width + x;

                resultPixels[targetIndex] =
                        newRgb;
            }
        }

        BufferedImage sharpened =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        sharpened.setRGB(
                0,
                0,
                width,
                height,
                resultPixels,
                0,
                width
        );

        File outputFile =
                new File(
                        new File(imagePath).getParent(),
                        "sharpen.jpg"
                );

        ImageIO.write(
                sharpened,
                "jpg",
                outputFile
        );
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