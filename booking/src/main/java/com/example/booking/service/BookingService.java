package com.example.booking.service;

import com.example.booking.entity.Booking;
import com.example.booking.web.model.BookingListResponse;
import com.example.booking.web.model.BookingRequest;
import com.example.booking.web.model.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse save(BookingRequest request);
    List<Booking> findAll();
}
