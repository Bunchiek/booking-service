package com.example.booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "unavailable_dates")
@Getter
@Setter
public class UnavailableDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkIn_Date")
    private LocalDate checkInDate;

    @Column(name = "checkOut_Date")
    private LocalDate checkOutDate;

    @ManyToMany(mappedBy = "dateSet")
    @JsonBackReference
    private Set<Room> roomSet;

}
