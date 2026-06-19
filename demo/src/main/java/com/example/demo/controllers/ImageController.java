package com.example.demo.controllers;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.service.ImageStorageService;
import com.example.demo.service.BrightnessService;
import com.example.demo.service.ContrastService;
import com.example.demo.service.GrayscaleService;
import com.example.demo.service.HorizontalFlipService;
import com.example.demo.service.ImageProcessingService;
import com.example.demo.service.VerticalFlipService;
import com.example.demo.service.ZoomService;
import com.example.demo.service.RotationService;
import com.example.demo.model.DetectedShape;
import com.example.demo.service.BlurService;
import com.example.demo.service.SharpenService;
import com.example.demo.service.LayeringService;
import com.example.demo.service.ShapeDetectionService;
//import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageStorageService imageStorageService;
    private final GrayscaleService grayscaleService;
    private final BrightnessService brightnessService;
    private final ContrastService contrastService;
    private final HorizontalFlipService horizontalFlipService;
    private final VerticalFlipService verticalFlipService;
    private final RotationService rotationService;
    private final ZoomService zoomService;
    private final BlurService blurService;
    private final SharpenService sharpenService;
    private final LayeringService layeringService;
    private final ShapeDetectionService shapeDetectionService;
    private ImageProcessingService imageProcessingService;

    public ImageController(ImageStorageService imageStorageService, GrayscaleService grayscaleService, ImageProcessingService imageProcessingService, BrightnessService brightnessService, ContrastService contrastService, HorizontalFlipService horizontalFlipService, VerticalFlipService verticalFlipService, RotationService rotationService, ZoomService zoomService, BlurService blurService, SharpenService sharpenService, LayeringService layeringService, ShapeDetectionService shapeDetectionService) {
        this.imageStorageService = imageStorageService;
        this.grayscaleService = grayscaleService;
        this.imageProcessingService = imageProcessingService;
        this.brightnessService = brightnessService;
        this.contrastService = contrastService;
        this.horizontalFlipService = horizontalFlipService;
        this.verticalFlipService = verticalFlipService;
        this.rotationService = rotationService;
        this.zoomService = zoomService;
        this.blurService = blurService;
        this.sharpenService = sharpenService;
        this.layeringService = layeringService;
        this.shapeDetectionService = shapeDetectionService;
    }

@PostMapping("/upload")
public ResponseEntity<String> uploadImage(
        @RequestParam("file") MultipartFile file) {

    System.out.println("Received file: " + file.getOriginalFilename());

    String imageId = imageStorageService.storeImage(file);

    return ResponseEntity.ok(
            "Image uploaded successfully. ID = " + imageId
    );
 }

@GetMapping("/{imageId}/download")
public ResponseEntity<Resource> downloadImage(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    Path path = Paths.get(imagePath);

    Resource resource =
            new UrlResource(path.toUri());

    if (!resource.exists()) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\""
                            + resource.getFilename()
                            + "\"")
            .body(resource);
}

@PostMapping("/{imageId}/grayscale")
public ResponseEntity<String> convertToGrayscale(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    String outputPath =
            grayscaleService.convertToGrayscale(imagePath);

    return ResponseEntity.ok(
    "Grayscale image created successfully "+ outputPath
    );
}

@PostMapping("/{imageId}/brightness")
public ResponseEntity<String> adjustBrightness(
        @PathVariable String imageId,
        @RequestParam int value)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    String outputPath =
            brightnessService.adjustBrightness(
                    imagePath,
                    value
            );

    return ResponseEntity.ok(
            "Brightness image created successfully " + outputPath
    );
}

@PostMapping("/{imageId}/contrast")
public ResponseEntity<String> adjustContrast(
        @PathVariable String imageId,
        @RequestParam double factor)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    String outputPath =
            contrastService.adjustContrast(
                    imagePath,
                    factor
            );

    return ResponseEntity.ok(
            "Contrast image created successfully " + outputPath
    );
}

@PostMapping("/{imageId}/horizontal-flip")
public ResponseEntity<String> horizontalFlip(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    horizontalFlipService.flip(imagePath);

    return ResponseEntity.ok(
            "Horizontal flip completed"
    );
}

@PostMapping("/{imageId}/vertical-flip")
public ResponseEntity<String> verticalFlip(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    verticalFlipService.flip(imagePath);

    return ResponseEntity.ok(
            "Vertical flip completed"
    );
}

@PostMapping("/{imageId}/rotate")
public ResponseEntity<String> rotate(
        @PathVariable String imageId,
        @RequestParam int angle)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    rotationService.rotate(
            imagePath,
            angle
    );

    return ResponseEntity.ok(
            "Rotation completed"
    );
}

@PostMapping("/{imageId}/zoom")
public ResponseEntity<String> zoom(
        @PathVariable String imageId,
        @RequestParam double factor)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    zoomService.zoom(
            imagePath,
            factor
    );

    return ResponseEntity.ok(
            "Zoom operation completed"
    );
}

@PostMapping("/{imageId}/blur")
public ResponseEntity<String> blur(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    blurService.blur(imagePath);

    return ResponseEntity.ok(
            "Blur completed successfully"
    );
}

@PostMapping("/{imageId}/sharpen")
public ResponseEntity<String> sharpen(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    sharpenService.sharpen(imagePath);

    return ResponseEntity.ok(
            "Image sharpened successfully");
}

@PostMapping("/{imageId}/layer")
public ResponseEntity<String> layer(
        @PathVariable String imageId,
        @RequestParam("overlayImage") MultipartFile overlayImage,
        @RequestParam int x,
        @RequestParam int y)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    layeringService.layer(
            imagePath,
            overlayImage,
            x,
            y);

    return ResponseEntity.ok(
            "Layering completed successfully");
}

@PostMapping("/{imageId}/detect-shapes")
public ResponseEntity<List<DetectedShape>>
detectShapes(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService
                    .getImagePath(imageId);

    return ResponseEntity.ok(shapeDetectionService.detectShapes(imagePath));
}

@PostMapping("/{imageId}/threshold")
public ResponseEntity<String> threshold(
        @PathVariable String imageId)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    shapeDetectionService
            .saveThresholdImage(imagePath);

    return ResponseEntity.ok(
            "Threshold image saved");
}

@PostMapping("/{imageId}/remove-background")
public ResponseEntity<String> removeBackground(
        @PathVariable String imageId,
        @RequestParam(defaultValue = "40") int threshold)
        throws IOException {

    String imagePath =
            imageStorageService.getImagePath(imageId);

    String outputPath =
            imageProcessingService.removeBackground(
                    imagePath,
                    threshold);

    return ResponseEntity.ok(
            "Background removed successfully: "
                    + outputPath);
}

}
