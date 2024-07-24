package com.example.booking.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {

    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
