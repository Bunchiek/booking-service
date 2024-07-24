package com.example.booking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "statistics")
public class Statistic {
    @Id
    private String id;
    private String eventType;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate eventDate;
}
