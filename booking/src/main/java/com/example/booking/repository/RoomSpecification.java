package com.example.booking.repository;

import com.example.booking.entity.Room;
import com.example.booking.entity.UnavailableDates;
import com.example.booking.web.model.filter.RoomFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter filter) {
        return Specification.where(byHotelId(filter.getHotelId()))
                .and(byRoomId(filter.getRoomId()))
                .and(byRoomDescription(filter.getDescription()))
                .and(byPrice(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byCapacity(filter.getCapacity()))
                .and(byDate(filter.getCheckInDate(), filter.getCheckOutDate()));
    }

    static Specification<Room> byRoomId(Long roomId) {
        return ((root, query, criteriaBuilder) -> {
            if(roomId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("Id"), roomId);
        });
    }

    static Specification<Room> byRoomDescription(String description) {
        return ((root, query, criteriaBuilder) -> {
            if(description == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("description"), description);
        });
    }

    static Specification<Room> byPrice(Double minPrice, Double maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            if(minPrice == null && maxPrice == null) {
                return null;
            }
            if (minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            if (maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        });

    }

    static Specification<Room> byCapacity(Integer capacity) {
        return ((root, query, criteriaBuilder) -> {
            if(capacity == null) {
                return null;
            }
            return criteriaBuilder.lessThan(root.get("capacity"), capacity);
        });
    }

    static Specification<Room> byDate(LocalDate checkInDate, LocalDate checkOutDate) {
        return (root, query, criteriaBuilder) -> {
            if (checkInDate == null || checkOutDate == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Room, UnavailableDates> join = root.join("dateSet", JoinType.LEFT);

            Predicate isNullUnavailableFrom = criteriaBuilder.isNull(join.get("unavailableFrom"));
            Predicate isNullUnavailableTo = criteriaBuilder.isNull(join.get("unavailableTo"));

            Predicate checkInBeforeUnavailableFrom = criteriaBuilder.lessThanOrEqualTo(join.get("unavailableFrom"), checkOutDate);
            Predicate checkOutAfterUnavailableTo = criteriaBuilder.greaterThanOrEqualTo(join.get("unavailableTo"), checkInDate);

            Predicate noOverlap = criteriaBuilder.or(
                    isNullUnavailableFrom,
                    isNullUnavailableTo,
                    criteriaBuilder.not(
                            criteriaBuilder.and(checkInBeforeUnavailableFrom, checkOutAfterUnavailableTo)
                    )
            );

            return criteriaBuilder.and(noOverlap);
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return ((root, query, criteriaBuilder) -> {
            if(hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
        });
    }
}
