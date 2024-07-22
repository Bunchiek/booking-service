package com.example.booking.service.impl;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Room;
import com.example.booking.entity.UnavailableDates;
import com.example.booking.entity.User;
import com.example.booking.exception.DataOccupiedException;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UnavailableDatesRepository;
import com.example.booking.service.BookingService;
import com.example.booking.service.RoomService;
import com.example.booking.service.UserService;
import com.example.booking.web.model.BookingListResponse;
import com.example.booking.web.model.BookingRequest;
import com.example.booking.web.model.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final UnavailableDatesRepository datesRepository;



    @Override
    public BookingResponse save(BookingRequest request) {
        Booking newBooking = new Booking();
        UnavailableDates date = new UnavailableDates();
        Room existedRoom = roomRepository.findById(request.getRoomId()).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Комната с ID {0} не найдена!",request.getRoomId())));
        if (existedRoom.getDateSet().stream()
                .anyMatch(bookDates -> (request.getCheckInDate().isAfter(bookDates.getUnavailableFrom())
                        && request.getCheckInDate().isBefore(bookDates.getUnavailableTo())
                || (request.getCheckOutDate().isAfter(bookDates.getUnavailableFrom())
                        && request.getCheckOutDate().isBefore(bookDates.getUnavailableTo()))))) {
            throw new DataOccupiedException(MessageFormat
                    .format("Даты с {0} по {1} уже заняты!", request.getCheckInDate(), request.getCheckOutDate()));
        }

        User exstedUser = userService.findById(request.getUserId());
        BookingResponse response = new BookingResponse();

        date.setUnavailableFrom(request.getCheckInDate());
        date.setUnavailableTo(request.getCheckOutDate());
        date.setRoomSet(Set.of(existedRoom));
        existedRoom.getDateSet().add(date);

        newBooking.setUser(exstedUser);
        newBooking.setRoom(existedRoom);
        newBooking.setCheckInDate(request.getCheckInDate());
        newBooking.setCheckOutDate(request.getCheckOutDate());
        datesRepository.save(date);
        bookingRepository.save(newBooking);
        roomRepository.save(existedRoom);

        response.setRoom(existedRoom);
        response.setUser(exstedUser);
        response.setDates(date);
        return response;
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }
}
