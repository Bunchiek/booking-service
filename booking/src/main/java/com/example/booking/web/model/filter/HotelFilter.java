package com.example.booking.web.model.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelFilter {

    private Integer pageSize;
    private Integer pageNumber;

    private Long hotelId;
    private String hotelName;
    private String hotelTitle;
    private String city;
    private String address;
    private Integer distanceFromCityCenter;
    private Double rating;
    private Integer countReview;

}
