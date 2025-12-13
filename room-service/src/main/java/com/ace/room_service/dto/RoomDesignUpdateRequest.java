package com.ace.room_service.dto;


import jakarta.validation.constraints.NotBlank;

public class RoomDesignUpdateRequest {

    @NotBlank
    private String generatedImageUrl;

    @NotBlank
    private String designPrompt;

    public RoomDesignUpdateRequest() {}

    public String getGeneratedImageUrl() {
        return generatedImageUrl;
    }

    public void setGeneratedImageUrl(String generatedImageUrl) {
        this.generatedImageUrl = generatedImageUrl;
    }

    public String getDesignPrompt() {
        return designPrompt;
    }

    public void setDesignPrompt(String designPrompt) {
        this.designPrompt = designPrompt;
    }
}

