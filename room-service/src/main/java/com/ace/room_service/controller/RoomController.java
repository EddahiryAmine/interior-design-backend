package com.ace.room_service.controller;

import com.ace.room_service.dto.CreateRoomRequest;
import com.ace.room_service.model.Room;
import com.ace.room_service.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.ok(room);
    }

    // Récupérer une pièce par id
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    // Liste des pièces d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Room>> getRoomsByUser(@PathVariable String userId) {
        List<Room> rooms = roomService.getRoomsByUser(userId);
        return ResponseEntity.ok(rooms);
    }
}
