package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.Comparator;

@Service
public class ImageStorageService {

    private final Path rootUploadPath;

    public ImageStorageService(
            @Value("${image.upload.path:uploads}") String uploadDir) {

        this.rootUploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(rootUploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    public String storeImage(MultipartFile file) {

        try {

            // Unique image ID
            String imageId = UUID.randomUUID().toString();

            // Create folder for this image
            Path imageFolder = rootUploadPath.resolve(imageId);
            Files.createDirectories(imageFolder);

            String originalFileName = file.getOriginalFilename();

            // Save image inside its folder
            Path targetFile = imageFolder.resolve(originalFileName);

            Files.copy(
                    file.getInputStream(),
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return imageId;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public String getImagePath(String imageId) {

    try {

        Path imageFolder = rootUploadPath.resolve(imageId);

        if (!Files.exists(imageFolder)) {
            throw new RuntimeException(
                    "Image folder not found: " + imageId
            );
        }

        return Files.list(imageFolder)
                 .max(Comparator.comparingLong(f -> f.toFile().lastModified()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "No image found for: " + imageId))
                .toString();

    } catch (IOException e) {
        throw new RuntimeException("Failed to locate image: " + imageId, e);
    }
  }
}