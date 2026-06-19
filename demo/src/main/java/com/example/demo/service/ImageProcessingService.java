package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageProcessingService {

    public BufferedImage removeBackground(
            BufferedImage image,
            int threshold) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB);

        Color background =
                new Color(image.getRGB(0, 0));

        int bgR = background.getRed();
        int bgG = background.getGreen();
        int bgB = background.getBlue();

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                Color pixel =
                        new Color(image.getRGB(x, y));

                int dr =
                        pixel.getRed() - bgR;

                int dg =
                        pixel.getGreen() - bgG;

                int db =
                        pixel.getBlue() - bgB;

                double distance =
                        Math.sqrt(
                                dr * dr +
                                dg * dg +
                                db * db);

                if (distance < threshold) {

                    output.setRGB(
                            x,
                            y,
                            0x00000000);

                } else {

                    output.setRGB(
                            x,
                            y,
                            image.getRGB(x, y));
                }
            }
        }

        return output;
    }

    public String removeBackground(
            String inputPath,
            int threshold)
            throws IOException {

        BufferedImage image =
                ImageIO.read(new File(inputPath));

        BufferedImage result =
                removeBackground(
                        image,
                        threshold);

        File inputFile =
                new File(inputPath);

        String fileName =
                inputFile.getName();

        int dotIndex =
                fileName.lastIndexOf('.');

        String baseName =
                dotIndex > 0
                        ? fileName.substring(0, dotIndex)
                        : fileName;

        String outputPath =
                inputFile.getParent()
                        + File.separator
                        + baseName
                        + "_background_removed.png";

        ImageIO.write(
                result,
                "png",
                new File(outputPath));

        return outputPath;
    }
}