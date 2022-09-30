package com.example.event_subscriber.controller;

import com.example.event_subscriber.dto.EventSubDto;
import com.example.event_subscriber.dto.SubscribeDto;
import com.example.event_subscriber.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/{email}")
    public ResponseEntity<List<SubscribeDto>> findEventsByEmail(@PathVariable String email) {
        return new ResponseEntity<>(subscribeService.findEventsByEmail(email), HttpStatus.OK);
    }

    @PostMapping  //on submit(email, event_id) from frontend
    public ResponseEntity<?> subscribeToEvent(@RequestBody EventSubDto eventSubDto) {
        return subscribeService.createASubscription(eventSubDto.getEmail(), eventSubDto.getEventId());
    }

    @DeleteMapping("/{email}&{sub_id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable String email, @PathVariable Long sub_id) {
        return subscribeService.deleteSubscription(email, sub_id);
    }
}
