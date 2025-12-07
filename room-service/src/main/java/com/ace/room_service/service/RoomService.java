package com.ace.room_service.service;

import com.ace.room_service.dto.CreateRoomRequest;
import com.ace.room_service.model.Room;

import java.util.List;

public interface RoomService {

    Room createRoom(CreateRoomRequest request);

    Room getRoomById(String id);

    List<Room> getRoomsByUser(String userId);
}
