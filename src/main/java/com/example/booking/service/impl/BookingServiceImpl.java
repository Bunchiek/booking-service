package com.example.booking.service.impl;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Room;
import com.example.booking.entity.UnavailableDates;
import com.example.booking.entity.User;
import com.example.booking.events.BookingEvent;
import com.example.booking.exception.DataOccupiedException;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UnavailableDatesRepository;
import com.example.booking.service.BookingService;
import com.example.booking.service.UserService;
import com.example.booking.web.model.BookingRequest;
import com.example.booking.web.model.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final UnavailableDatesRepository datesRepository;

    @Value("${app.kafka.kafkaBookingTopic}")
    private String kafkaBookingTopic;

    private final KafkaTemplate<String, BookingEvent> kafkaBookingEventProducerFactory;

    @Override
    public BookingResponse save(BookingRequest request) {
        Room existedRoom = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Комната с ID {0} не найдена!", request.getRoomId())));

        if (isRoomUnavailable(existedRoom, request.getCheckInDate(), request.getCheckOutDate())) {
            throw new DataOccupiedException(MessageFormat.format("Даты с {0} по {1} уже заняты!",
                    request.getCheckInDate(), request.getCheckOutDate()));
        }

        User existedUser = userService.findById(request.getUserId());

        UnavailableDates date = createUnavailableDates(request, existedRoom);
        Booking newBooking = createBooking(request, existedUser, existedRoom);

        saveEntities(date, newBooking, existedRoom);
        sendBookingEvent(existedUser, newBooking);

        return createBookingResponse(existedUser, existedRoom, date);
    }

    private boolean isRoomUnavailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return room.getDateSet().stream()
                .anyMatch(bookDates -> isDateOverlapping(checkIn, checkOut, bookDates));
    }

    private UnavailableDates createUnavailableDates(BookingRequest request, Room room) {
        UnavailableDates date = new UnavailableDates();
        date.setUnavailableFrom(request.getCheckInDate());
        date.setUnavailableTo(request.getCheckOutDate());
        date.setRoomSet(Set.of(room));
        room.getDateSet().add(date);
        return date;
    }

    private Booking createBooking(BookingRequest request, User user, Room room) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        return booking;
    }

    private void saveEntities(UnavailableDates date, Booking booking, Room room) {
        datesRepository.save(date);
        bookingRepository.save(booking);
        roomRepository.save(room);
    }

    private void sendBookingEvent(User user, Booking booking) {
        BookingEvent event = new BookingEvent();
        event.setUserId(user.getId());
        event.setCheckOutDate(booking.getCheckOutDate());
        event.setCheckInDate(booking.getCheckInDate());
        kafkaBookingEventProducerFactory.send(kafkaBookingTopic, event);
    }

    private BookingResponse createBookingResponse(User user, Room room, UnavailableDates date) {
        BookingResponse response = new BookingResponse();
        response.setRoom(room);
        response.setUser(user);
        response.setDates(date);
        return response;
    }

    private boolean isDateOverlapping(LocalDate checkIn, LocalDate checkOut, UnavailableDates bookDates) {
        return !checkIn.isAfter(bookDates.getUnavailableTo()) && !checkOut.isBefore(bookDates.getUnavailableFrom());
    }


    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }
}
