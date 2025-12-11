package com.ace.designservice.service;


import com.ace.designservice.client.RoomServiceClient;
import com.ace.designservice.client.StyleCatalogClient;
import com.ace.designservice.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.StringJoiner;

@Service
public class PromptService {

    private final RoomServiceClient roomServiceClient;
    private final StyleCatalogClient styleCatalogClient;

    public PromptService(RoomServiceClient roomServiceClient,
                         StyleCatalogClient styleCatalogClient) {
        this.roomServiceClient = roomServiceClient;
        this.styleCatalogClient = styleCatalogClient;
    }

    public GenerateDesignResponse generatePrompt(GenerateDesignRequest request) {

        RoomSummary room = roomServiceClient.getRoomById(request.getRoomId());
        if (room == null) {
            throw new RuntimeException("Room not found with id: " + request.getRoomId());
        }

        StyleSummary style = null;
        if (room.getStyleId() != null) {
            style = styleCatalogClient.getStyleById(room.getStyleId());
        }

        ColorPaletteSummary palette = null;
        if (room.getColorPaletteId() != null) {
            palette = styleCatalogClient.getPaletteById(room.getColorPaletteId());
        }

        String roomType = room.getDetectedRoomType(); // bedroom, living, etc.

        String prompt = buildPrompt(roomType, style, palette, request.getExtraPrompt());

        String styleName = style != null ? style.getName() : null;
        String paletteName = palette != null ? palette.getName() : null;

        return new GenerateDesignResponse(
                room.getId(),
                roomType,
                styleName,
                paletteName,
                prompt
        );
    }

    private String buildPrompt(String roomType,
                               StyleSummary style,
                               ColorPaletteSummary palette,
                               String extraPrompt) {

        StringJoiner sj = new StringJoiner(", ");

        sj.add("high quality 4k interior design render");

        if (style != null && StringUtils.hasText(style.getName())) {
            sj.add("in " + style.getName());
        }

        if (StringUtils.hasText(roomType)) {
            sj.add("of a " + roomType.replace("_", " "));
        }

        if (style != null && style.getKeywords() != null && !style.getKeywords().isEmpty()) {
            sj.add(String.join(", ", style.getKeywords()));
        }

        if (palette != null && palette.getColors() != null && !palette.getColors().isEmpty()) {
            sj.add("color palette: " + String.join(" ", palette.getColors()));
        }

        if (StringUtils.hasText(extraPrompt)) {
            sj.add(extraPrompt);
        }

        return sj.toString();
    }
}

