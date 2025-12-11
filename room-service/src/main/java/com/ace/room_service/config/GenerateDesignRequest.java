package com.ace.room_service.config;

import jakarta.validation.constraints.NotBlank;

public class GenerateDesignRequest {

    @NotBlank
    private String roomId;

    private String extraPrompt;

    public GenerateDesignRequest() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getExtraPrompt() {
        return extraPrompt;
    }

    public void setExtraPrompt(String extraPrompt) {
        this.extraPrompt = extraPrompt;
    }
}
