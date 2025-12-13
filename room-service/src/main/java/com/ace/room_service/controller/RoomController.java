package com.ace.room_service.controller;

import com.ace.room_service.dto.CreateRoomRequest;
import com.ace.room_service.dto.RoomDesignUpdateRequest;
import com.ace.room_service.model.Room;
import com.ace.room_service.repository.RoomRepository;
import com.ace.room_service.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    public RoomController(RoomService roomService, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Room>> getRoomsByUser(@PathVariable String userId) {
        List<Room> rooms = roomService.getRoomsByUser(userId);
        return ResponseEntity.ok(rooms);
    }
    @PatchMapping("/{id}/design")
    public ResponseEntity<?> updateDesign(
            @PathVariable String id,
            @Valid @RequestBody RoomDesignUpdateRequest req
    ) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));

        room.setGeneratedImageUrl(req.getGeneratedImageUrl());
        room.setDesignPrompt(req.getDesignPrompt());

        Room saved = roomRepository.save(room);
        return ResponseEntity.ok(saved);
    }

}
