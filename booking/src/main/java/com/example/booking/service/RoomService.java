package com.example.booking.service;

import com.example.booking.entity.Room;
import com.example.booking.web.model.RoomRequest;
import com.example.booking.web.model.filter.RoomFilter;

import java.util.List;


public interface RoomService {
    List<Room> filterBy(RoomFilter filter);
    Room findById(Long id);
    Room save(RoomRequest request);
    Room update(Room room);
    void deleteById(Long id);
}
