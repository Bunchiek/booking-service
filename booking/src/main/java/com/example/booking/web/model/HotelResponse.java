package com.example.booking.web.model;

import com.example.booking.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {

    private String id;
    private String name;
    private String title;
    private String city;
    private String address;
    private Integer distanceFromCityCenter;
    private Double rating;
    private Integer countReview;
    private List<Room> rooms;
}
