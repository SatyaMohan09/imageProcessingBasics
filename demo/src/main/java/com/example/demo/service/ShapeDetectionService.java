package com.example.demo.service;

import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.example.demo.model.BoundingBox;
import com.example.demo.model.Component;
import com.example.demo.model.DetectedShape;
import com.example.demo.model.Point;

@Service
public class ShapeDetectionService {

    public List<DetectedShape> detectShapes(String imagePath) throws IOException {

    BufferedImage image = ImageIO.read(new File(imagePath));

    BufferedImage binary = threshold(image);

    List<Component> components =
            findComponents(binary);

    List<DetectedShape> results = new ArrayList<>();

    for (Component component : components) {

        BoundingBox box = calculateBoundingBox(component);

        String shape = classifyShape(component);

        results.add(new DetectedShape(shape, component.getArea(), box.getWidth(), box.getHeight()));
    }

    return results;
}

    private BufferedImage threshold(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage binary = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);

        int thresholdValue = 128;

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                int gray = (r + g + b) / 3;

                if (gray > thresholdValue) {

                    binary.setRGB(x,y,0xFFFFFFFF); // White

                } else {

                    binary.setRGB(x,y,0xFF000000); // Black
                }
            }
        }

        return binary;
    }


    private List<Component> findComponents(BufferedImage binary) {

    int width = binary.getWidth();
    int height = binary.getHeight();

    boolean[][] visited = new boolean[height][width];

    List<Component> components = new ArrayList<>();

    for (int y = 0; y < height; y++) {

        for (int x = 0; x < width; x++) {

            if (visited[y][x]) {
                continue;
            }

            int rgb = binary.getRGB(x, y);

            // Skip white pixels
            if (rgb != 0xFFFFFFFF) {
                continue;
            }

            Component component = bfs(binary, x, y, visited);

            if (component.getArea() > 50) {
                components.add(component);
            }
        }
    }

    return components;
}

private Component bfs(
        BufferedImage binary,
        int startX,
        int startY,
        boolean[][] visited) {

    int width = binary.getWidth();
    int height = binary.getHeight();

    Queue<Point> queue = new LinkedList<>();

    Component component = new Component();

    queue.offer(new Point(startX, startY));

    visited[startY][startX] = true;

    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    while (!queue.isEmpty()) {

        Point current =queue.poll();

        component.addPixel(current);

        for (int i = 0; i < 4; i++) {

            int nx = current.getX() + dx[i];

            int ny = current.getY() + dy[i];

            if (nx < 0 ||
                nx >= width ||
                ny < 0 ||
                ny >= height) {

                continue;
            }

            if (visited[ny][nx]) {
                continue;
            }

            int rgb = binary.getRGB(nx, ny);

            if (rgb != 0xFF000000) {
                continue;
            }

            visited[ny][nx] = true;

            queue.offer(new Point(nx, ny));
        }
    }

    return component;
}

private BoundingBox calculateBoundingBox(
        Component component) {

    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;

    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;

    for (Point point : component.getPixels()) {

        minX = Math.min(minX, point.getX());
        maxX = Math.max(maxX, point.getX());

        minY = Math.min(minY, point.getY());
        maxY = Math.max(maxY, point.getY());
    }

    return new BoundingBox(
            minX,
            maxX,
            minY,
            maxY
    );
}


private String classifyShape(Component component) {

    BoundingBox box = calculateBoundingBox(component);

    int area = component.getArea();

    int width = box.getWidth();

    int height = box.getHeight();

    double fillRatio = area /(double)(width * height);

    if (fillRatio > 0.90) {

        if (Math.abs(width - height) < 10) {
            return "SQUARE";
        }

        return "RECTANGLE";
    }

    if (fillRatio > 0.70 &&
        fillRatio < 0.90) {

        return "CIRCLE";
    }

    if (fillRatio > 0.40 &&
        fillRatio < 0.70) {

        return "TRIANGLE";
    }

    return "UNKNOWN";
}

public void saveThresholdImage(String imagePath)
        throws IOException {

    BufferedImage image =
            ImageIO.read(new File(imagePath));

    BufferedImage binary =
            threshold(image);

    File outputFile =
            new File(
                    new File(imagePath).getParent(),
                    "threshold.png");

    ImageIO.write(
            binary,
            "png",
            outputFile);
}

}