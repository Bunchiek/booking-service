package com.example.booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "unavailable_from")
    private LocalDate unavailableFrom;

    @Column(name = "unavailable_to")
    private LocalDate unavailableTo;

    @ManyToMany(mappedBy = "dateSet")
    @JsonBackReference
    private Set<Room> roomSet;

}
