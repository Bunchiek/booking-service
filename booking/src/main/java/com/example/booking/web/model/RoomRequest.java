package com.example.booking.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    private String name;
    private String description;
    private Integer number;
    private Double price;
    private Integer capacity;
    private Long hotelId;
}
