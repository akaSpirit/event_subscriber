package com.example.event_subscriber.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Subscribe {
    private Long id;
    private Event event;
    @JsonProperty("event_id")
    private Long eventId;
    private String email;
    private LocalDateTime registerDateTime;
}
