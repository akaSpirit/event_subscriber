package com.example.event_subscriber.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {
    private Long id;
    private EventDto event;
    private String email;
    @JsonProperty("register_date_time")
    private LocalDateTime registerDateTime;

}
