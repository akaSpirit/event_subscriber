package com.example.event_subscriber.service;

import com.example.event_subscriber.dao.EventDao;
import com.example.event_subscriber.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventDao eventDao;

    public List<EventDto> getAll() {
        return eventDao.getAllEvents();
    }

    public List<EventDto> getAllBySubscribe(String email){
        return eventDao.findBySubscribeEmail(email).stream()
                .map(event -> EventDto.builder()
                        .id(event.getId())
                        .dateTime(event.getDateTime())
                        .name(event.getName())
                        .description(event.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
