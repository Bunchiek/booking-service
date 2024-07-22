package com.example.booking.repository;

import com.example.booking.entity.UnavailableDates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnavailableDatesRepository extends JpaRepository<UnavailableDates, Long> {
}
