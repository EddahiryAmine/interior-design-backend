package com.ace.room_service.repository;

import com.ace.room_service.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {

    List<Room> findByUserId(String userId);
}
