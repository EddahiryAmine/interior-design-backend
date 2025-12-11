package com.ace.room_service.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRoomRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String imageUrl;

    private String desiredRoomType;
    private String styleId;
    private String colorPaletteId;

    public CreateRoomRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
