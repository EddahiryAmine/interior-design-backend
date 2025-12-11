package com.ace.room_service.service;

import com.ace.room_service.client.AiRoomServiceClient;
import com.ace.room_service.dto.AiPredictionResponse;
import com.ace.room_service.dto.CreateRoomRequest;
import com.ace.room_service.model.Room;
import com.ace.room_service.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final AiRoomServiceClient aiRoomServiceClient;

    public RoomServiceImpl(RoomRepository roomRepository,
                           AiRoomServiceClient aiRoomServiceClient) {
        this.roomRepository = roomRepository;
        this.aiRoomServiceClient = aiRoomServiceClient;
    }

    @Override
    @Transactional
    public Room createRoom(CreateRoomRequest request) {

        AiPredictionResponse prediction = aiRoomServiceClient.predictRoomType(request.getImageUrl());

        log.info("AI prediction for image {} => type={}, confidence={}",
                request.getImageUrl(), prediction.getRoomType(), prediction.getConfidence());

        Room room = new Room();
        room.setUserId(request.getUserId());
        room.setOriginalImageUrl(request.getImageUrl());

        room.setDetectedRoomType(prediction.getRoomType());
        room.setDetectedConfidence(prediction.getConfidence());

        room.setDesiredRoomType(request.getDesiredRoomType());
        room.setStyleId(request.getStyleId());
        room.setColorPaletteId(request.getColorPaletteId());

        // 3) Sauvegarder en DB
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    public List<Room> getRoomsByUser(String userId) {
        return roomRepository.findByUserId(userId);
    }
}
