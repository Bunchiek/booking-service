package com.example.booking.web.controller;

import com.example.booking.entity.Room;
import com.example.booking.mapper.RoomMapper;
import com.example.booking.service.RoomService;
import com.example.booking.web.model.*;
import com.example.booking.web.model.filter.RoomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;


    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<RoomResponse>> filterBy(RoomFilter filter) {
        return ResponseEntity.ok(roomService.filterBy(filter).stream().map(roomMapper::roomToResponse).toList());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(roomMapper.roomToResponse(roomService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody RoomRequest request) {
        Room newRoom = roomService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.roomToResponse(newRoom));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable("id") Long roomId, @RequestBody RoomRequest request) {
        Room updatedRoom = roomService.update(roomMapper.requestToRoom(roomId, request));
        return ResponseEntity.ok(roomMapper.roomToResponse(updatedRoom));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
