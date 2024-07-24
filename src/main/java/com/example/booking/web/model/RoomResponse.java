package com.example.booking.web.model;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Hotel;
import com.example.booking.entity.UnavailableDates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    private Long id;

    private String name;

    private String description;

    private Integer number;

    private Double price;

    private Integer capacity;

    private Hotel hotel;

    private Set<UnavailableDates> dateSet;

    private List<Booking> bookings;

}
