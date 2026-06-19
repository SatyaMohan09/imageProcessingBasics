package com.example.demo.dto;

public class ImageUploadResponse {

    private String imageId;
    private String fileName;

    public ImageUploadResponse() {
    }

    public ImageUploadResponse(
            String imageId,
            String fileName) {

        this.imageId = imageId;
        this.fileName = fileName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}