package com.brujua.comics.controller.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CollaboratorsResponse {
    private final Instant last_sync;
    @JsonUnwrapped
    private final Map<String, List<String>> collaboratorsByRole;
}
