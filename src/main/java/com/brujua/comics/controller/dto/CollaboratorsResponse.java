package com.brujua.comics.controller.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@AllArgsConstructor
public class CollaboratorsResponse {
    private final Instant lastSync;

    private final Map<String, Set<String>> collaboratorsByRole;

    public Instant getLastSync() {
        return lastSync;
    }

    @JsonAnyGetter
    public Map<String, Set<String>> getCollaboratorsByRole() {
        return collaboratorsByRole;
    }
}
