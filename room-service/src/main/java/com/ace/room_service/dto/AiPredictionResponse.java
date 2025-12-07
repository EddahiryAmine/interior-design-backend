package com.ace.room_service.dto;

public class AiPredictionResponse {

    private String roomType;
    private double confidence;

    public AiPredictionResponse() {
    }

    public AiPredictionResponse(String roomType, double confidence) {
        this.roomType = roomType;
        this.confidence = confidence;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
