package com.example.booking.web.model.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RoomFilter {

    private Integer pageSize;
    private Integer pageNumber;

    private Long roomId;
    private String description;
    private Double minPrice;
    private Double maxPrice;
    private Integer capacity;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long hotelId;

}
