package com.stefanoni.planner.participant;

import java.util.UUID;

public record ParticipantData (UUID id, String name, String email, Boolean isConfirmed){}
