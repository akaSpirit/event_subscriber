package com.example.event_subscriber.service;

import com.example.event_subscriber.dao.EventDao;
import com.example.event_subscriber.dao.SubscribeDao;
import com.example.event_subscriber.dto.EventDto;
import com.example.event_subscriber.dto.SubscribeDto;
import com.example.event_subscriber.model.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeDao subscribeDao;
    private final EventDao eventDao;

    public ResponseEntity<?> createASubscription(String email, Long eventId) {
        Long id = subscribeDao.create(email, eventId);
        Subscribe subscribe = subscribeDao.findById(id).orElseThrow();
        EventDto event = eventDao.findById(subscribe.getEventId()).get();

        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(SubscribeDto.builder()
                .id(subscribe.getId())
                .event(EventDto.builder()
                        .id(event.getId())
                        .name(event.getName())
                        .description(event.getDescription())
                        .dateTime(event.getDateTime())
                        .build())
                .email(subscribe.getEmail())
                .registerDateTime(subscribe.getRegisterDateTime())
                .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<String> deleteSubscription(String email, Long eventId) {
        Subscribe subscribe = subscribeDao.findByEmailAndEventId(email, eventId).orElseThrow();
        return new ResponseEntity<>(subscribeDao.delete(email, eventId), HttpStatus.OK);
    }

    private SubscribeDto getSubscription(Subscribe subscribe) {
        var event = eventDao.findById(subscribe.getEventId()).get();
        return SubscribeDto.builder()
                .id(subscribe.getId())
                .event(EventDto.builder()
                        .id(event.getId())
                        .dateTime(event.getDateTime())
                        .name(event.getName())
                        .description(event.getDescription())
                        .build())
                .email(subscribe.getEmail())
                .registerDateTime(subscribe.getRegisterDateTime())
                .build();
    }

    public List<SubscribeDto> findEventsByEmail(String email) {
        var subs = subscribeDao.findEventsByEmail(email);
        return subs.stream()
                .map(this::getSubscription)
                .collect(Collectors.toList());
    }
}