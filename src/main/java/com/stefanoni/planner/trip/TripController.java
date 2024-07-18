package com.stefanoni.planner.trip;

import com.stefanoni.planner.activity.ActivityData;
import com.stefanoni.planner.activity.ActivityRequestPayload;
import com.stefanoni.planner.activity.ActivityResponse;
import com.stefanoni.planner.link.*;
import com.stefanoni.planner.participant.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService service;

    @PostMapping
    @Transactional
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        return ResponseEntity.ok(this.service.createTrip(payload));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.getTripDetails(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        return ResponseEntity.ok(this.service.updateTrip(id, payload));
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.confirmTrip(id));
    }

    @PostMapping("/{id}/activities")
    @Transactional
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        return ResponseEntity.ok(this.service.registerActivity(id, payload));
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.getAllActivities(id));
    }

    @PostMapping("/{id}/invite")
    @Transactional
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        return ResponseEntity.ok(this.service.inviteParticipant(id, payload));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.getAllParticipants(id));
    }

    @PostMapping("/{id}/links")
    @Transactional
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        return ResponseEntity.ok(this.service.registerLink(id, payload));
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.getAllLinks(id));
    }
}
