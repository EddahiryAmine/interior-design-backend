package com.ace.designservice.dto;


public class GenerateDesignResponse {

    private String roomId;
    private String roomType;
    private String styleName;
    private String paletteName;

    private String prompt; // le prompt complet à envoyer au modèle de génération IA

    public GenerateDesignResponse() {
    }

    public GenerateDesignResponse(String roomId, String roomType,
                                  String styleName, String paletteName,
                                  String prompt) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.styleName = styleName;
        this.paletteName = paletteName;
        this.prompt = prompt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getPaletteName() {
        return paletteName;
    }

    public void setPaletteName(String paletteName) {
        this.paletteName = paletteName;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
