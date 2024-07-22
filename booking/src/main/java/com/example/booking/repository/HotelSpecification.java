package com.example.booking.repository;

import com.example.booking.entity.Hotel;
import com.example.booking.web.model.filter.HotelFilter;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {

    static Specification<Hotel> withFilter(HotelFilter filter) {
        return Specification.where(byHotelId(filter.getHotelId()))
                .and(byHotelName(filter.getHotelName()))
                .and(byHotelTitle(filter.getHotelTitle()))
                .and(byCity(filter.getCity()))
                .and(byAddress(filter.getAddress()))
                .and(byDistanceFromCityCenter(filter.getDistanceFromCityCenter()))
                .and(byRating(filter.getRating()));
    }

    static Specification<Hotel> byRating(Double rating) {
        return ((root, query, criteriaBuilder) -> {
            if(rating == null) {
                return null;
            }
            return criteriaBuilder.greaterThan(root.get("rating"), rating);
        });
    }

    static Specification<Hotel> byDistanceFromCityCenter(Integer distanceFromCityCenter) {
        return ((root, query, criteriaBuilder) -> {
            if(distanceFromCityCenter == null) {
                return null;
            }
            return criteriaBuilder.lessThan(root.get("distanceFromCityCenter"), distanceFromCityCenter);
        });
    }

    static Specification<Hotel> byAddress(String address) {
        return ((root, query, criteriaBuilder) -> {
            if(address == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("address"), address);
        });

    }

    static Specification<Hotel> byCity(String city) {
        return ((root, query, criteriaBuilder) -> {
            if(city == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("city"), city);
        });
    }

    static Specification<Hotel> byHotelTitle(String hotelTitle) {
        return ((root, query, criteriaBuilder) -> {
            if(hotelTitle == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("title"), hotelTitle);
        });
    }

    static Specification<Hotel> byHotelName(String hotelName) {
        return ((root, query, criteriaBuilder) -> {
            if(hotelName == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("name"), hotelName);
        });
    }

    static Specification<Hotel> byHotelId(Long hotelId) {
        return ((root, query, criteriaBuilder) -> {
            if(hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), hotelId);
        });
    }
}
