package com.example.booking.mapper;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.Room;
import com.example.booking.web.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room requestToRoom(RoomRequest request);

    @Mapping(source = "roomId", target = "id")
    Room requestToRoom(Long roomId, RoomRequest request);

    RoomResponse roomToResponse(Room room);


}
