package com.example.booking.service.impl;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.Room;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.exception.ServerErrorException;
import com.example.booking.mapper.RoomMapper;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.RoomSpecification;
import com.example.booking.service.HotelService;
import com.example.booking.service.RoomService;
import com.example.booking.utils.BeanUtils;
import com.example.booking.web.model.RoomRequest;
import com.example.booking.web.model.filter.RoomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;

    private final HotelService hotelService;

    private final RoomMapper roomMapper;


    @Override
    public List<Room> filterBy(RoomFilter filter) {
        if (filter.getPageNumber() == null || filter.getPageSize() == null) {
            throw new ServerErrorException("Необходимо задать pageSize и pageNumber");
        }
        return repository.findAll(RoomSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
    }

    @Override
    public Room findById(Long id) {
        return repository.findById(id).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Комната с ID {0} не найдена!",id)));
    }

    @Override
    public Room save(RoomRequest request) {
        Room newRoom = roomMapper.requestToRoom(request);
        Hotel hotel = hotelService.findById(request.getHotelId());
        newRoom.setHotel(hotel);
        return repository.save(newRoom);
    }

    @Override
    public Room update(Room room) {
        Room existedRoom = findById(room.getId());
        BeanUtils.copyNonNullProperties(room, existedRoom);
        return repository.save(existedRoom);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
