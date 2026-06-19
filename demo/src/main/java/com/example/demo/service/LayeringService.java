package com.example.demo.service;

import com.example.demo.utils.ExtractPixels;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class LayeringService {

    public void layer(
            String backgroundPath,
            MultipartFile overlayFile,
            int startX,
            int startY)
            throws IOException {

        BufferedImage background =
                ImageIO.read(
                        new File(backgroundPath)
                );

        BufferedImage overlay =
                ImageIO.read(
                        overlayFile.getInputStream()
                );

        int backgroundWidth =
                background.getWidth();

        int backgroundHeight =
                background.getHeight();

        int overlayWidth =
                overlay.getWidth();

        int overlayHeight =
                overlay.getHeight();

        ExtractPixels extractor =
                new ExtractPixels();

        int[] backgroundPixels =
                extractor.extractPixels(background);

        int[] overlayPixels =
                extractor.extractPixels(overlay);

        for (int y = 0; y < overlayHeight; y++) {

            for (int x = 0; x < overlayWidth; x++) {

                int targetX =
                        startX + x;

                int targetY =
                        startY + y;

                if (targetX < 0
                        || targetX >= backgroundWidth
                        || targetY < 0
                        || targetY >= backgroundHeight) {
                    continue;
                }

                int overlayIndex =
                        y * overlayWidth + x;

                int backgroundIndex =
                        targetY * backgroundWidth
                                + targetX;

                int overlayRgb =
                        overlayPixels[overlayIndex];

                backgroundPixels[backgroundIndex] =
                        overlayRgb;
            }
        }

        background.setRGB(
                0,
                0,
                backgroundWidth,
                backgroundHeight,
                backgroundPixels,
                0,
                backgroundWidth
        );

        File outputFile =
                new File(
                        new File(backgroundPath)
                                .getParent(),
                        "layered.png"
                );

        ImageIO.write(
                background,
                "png",
                outputFile
        );
    }
}