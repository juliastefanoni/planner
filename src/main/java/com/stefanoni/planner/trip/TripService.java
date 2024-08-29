package com.stefanoni.planner.trip;

import com.stefanoni.planner.activity.ActivityData;
import com.stefanoni.planner.activity.ActivityRequestPayload;
import com.stefanoni.planner.activity.ActivityResponse;
import com.stefanoni.planner.activity.ActivityService;
import com.stefanoni.planner.exceptions.InvalidFieldsException;
import com.stefanoni.planner.exceptions.NotFoundException;
import com.stefanoni.planner.link.LinkData;
import com.stefanoni.planner.link.LinkRequestPayload;
import com.stefanoni.planner.link.LinkResponse;
import com.stefanoni.planner.link.LinkService;
import com.stefanoni.planner.participant.ParticipantCreateResponse;
import com.stefanoni.planner.participant.ParticipantData;
import com.stefanoni.planner.participant.ParticipantRequestPayload;
import com.stefanoni.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.stefanoni.planner.utils.Constants.*;

@Service
public class TripService {

    @Autowired
    private TripRepository repository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    public TripCreateResponse createTrip(TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        if(newTrip.validateFields().equals(Boolean.FALSE)) {
            throw new InvalidFieldsException(TRIP_DATE_INVALID);
        }

        this.repository.save(newTrip);

        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return new TripCreateResponse(newTrip.getId());
    }

    public Trip getTripDetails(UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isEmpty()) throw new NotFoundException(TRIP_NOT_FOUND);

        return trip.get();
    }

    public Trip updateTrip(UUID id, TripRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            if(rawTrip.validateFields().equals(Boolean.FALSE)) {
                throw new InvalidFieldsException(TRIP_DATE_INVALID);
            }

            this.repository.save(rawTrip);

            return rawTrip;
        }

        throw new NotFoundException(TRIP_NOT_FOUND);
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setIsConfirmed(true);

            this.repository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return rawTrip;
        }

        throw new NotFoundException(TRIP_NOT_FOUND);
    }

    public ActivityResponse registerActivity(UUID id, ActivityRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            return this.activityService.registerActivity(payload, rawTrip);
        }

        throw new NotFoundException(TRIP_NOT_FOUND);
    }

    public List<ActivityData> getAllActivities(UUID id) {
        return this.activityService.getAllActivitiesFromId(id);
    }

    public ParticipantCreateResponse inviteParticipant(UUID id, ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if (rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return participantResponse;
        }

        throw new NotFoundException(TRIP_NOT_FOUND);
    }

    public List<ParticipantData> getAllParticipants(UUID id) {
        return this.participantService.getAllParticipantsFromEvent(id);
    }

    public LinkResponse registerLink(UUID id, LinkRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            return this.linkService.registerLink(payload, rawTrip);

        }

        throw new NotFoundException(TRIP_NOT_FOUND);
    }

    public List<LinkData> getAllLinks(UUID id) {
        return this.linkService.getAllLinksFromTrip(id);
    }
}
