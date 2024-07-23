package com.example.booking.service;

import com.example.booking.entity.Statistic;
import com.example.booking.events.BookingEvent;
import com.example.booking.events.RegistrationEvent;
import com.example.booking.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final StatisticRepository statisticRepository;

    @KafkaListener(topics = "${app.kafka.kafkaRegisterTopic}",
            groupId = "${app.kafka.kafkaBookingGroupId}",
            containerFactory = "kafkaRegistrationEventConcurrentKafkaListenerContainerFactory")
    public void handleUserRegistered(RegistrationEvent event) {
        Statistic statistic = new Statistic();
        statistic.setEventType("USER_REGISTERED");
        statistic.setUserId(event.getUserId());
        statistic.setEventDate(LocalDate.now());
        statisticRepository.save(statistic);

        log.info("Received event: {}", event);
    }


    @KafkaListener(topics = "${app.kafka.kafkaBookingTopic}",
            groupId = "${app.kafka.kafkaBookingGroupId}",
            containerFactory = "kafkaBookingEventConcurrentKafkaListenerContainerFactory")
    public void handleRoomBooked(BookingEvent event) {
        Statistic statistic = new Statistic();
        statistic.setEventType("ROOM_BOOKED");
        statistic.setUserId(event.getUserId());
        statistic.setCheckInDate(event.getCheckInDate());
        statistic.setCheckOutDate(event.getCheckOutDate());
        statistic.setEventDate(LocalDate.now());
        statisticRepository.save(statistic);

        log.info("Received event: {}", event);
    }


    public void exportStatisticsToCsv(String filePath) {
        List<Statistic> statistics = statisticRepository.findAll();
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID,EventType,UserID,CheckInDate,CheckOutDate,EventDate\n");

            for (Statistic stat : statistics) {
                sb.append(stat.getId()).append(",");
                sb.append(stat.getEventType()).append(",");
                sb.append(stat.getUserId()).append(",");
                sb.append(stat.getCheckInDate() != null ? stat.getCheckInDate() : "").append(",");
                sb.append(stat.getCheckOutDate() != null ? stat.getCheckOutDate() : "").append(",");
                sb.append(stat.getEventDate()).append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
