package com.ace.room_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "rooms")
public class Room {

    @Id
    private String id;

    private String userId;

    private String originalImageUrl;

    private String detectedRoomType;
    private double detectedConfidence;

    private String desiredRoomType;

    private String styleId;
    private String colorPaletteId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getDetectedRoomType() {
        return detectedRoomType;
    }

    public void setDetectedRoomType(String detectedRoomType) {
        this.detectedRoomType = detectedRoomType;
    }

    public double getDetectedConfidence() {
        return detectedConfidence;
    }

    public void setDetectedConfidence(double detectedConfidence) {
        this.detectedConfidence = detectedConfidence;
    }

    public String getDesiredRoomType() {
        return desiredRoomType;
    }

    public void setDesiredRoomType(String desiredRoomType) {
        this.desiredRoomType = desiredRoomType;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getColorPaletteId() {
        return colorPaletteId;
    }

    public void setColorPaletteId(String colorPaletteId) {
        this.colorPaletteId = colorPaletteId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
