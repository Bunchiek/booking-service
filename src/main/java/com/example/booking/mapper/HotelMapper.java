package com.example.booking.mapper;

import com.example.booking.entity.Hotel;
import com.example.booking.web.model.HotelListResponse;
import com.example.booking.web.model.HotelRequest;
import com.example.booking.web.model.HotelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    Hotel requestToHotel(HotelRequest request);

    @Mapping(source = "hotelId", target = "id")
    Hotel requestToHotel(Long hotelId, HotelRequest request);

    HotelResponse hotelToResponse(Hotel hotel);

    default HotelListResponse hotelListToListResponse(List<Hotel> hotels) {
        HotelListResponse response = new HotelListResponse();
        response.setHotels(hotels.stream()
                .map(this::hotelToResponse).toList());
        return response;
    }

}
