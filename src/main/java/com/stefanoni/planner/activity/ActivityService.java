package com.stefanoni.planner.activity;

import com.stefanoni.planner.exceptions.InvalidFieldsException;
import com.stefanoni.planner.trip.Trip;
import com.stefanoni.planner.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        if(newActivity.getOccursAt().isBefore(trip.getStartsAt()) || newActivity.getOccursAt().isAfter(trip.getEndsAt())) {
            throw new InvalidFieldsException(Constants.ACTIVITY_DATE_INVALID);
        }

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
