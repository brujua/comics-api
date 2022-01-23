package com.brujua.comics.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toMap;

@Builder
@Getter
public class Character {

    private static final int MAX_SYNC_INTERVAL_DAYS = 3;

    private final String name;
    private final String id;
    private final Instant lastSync;
    private final List<Comic> comics;

    public boolean exceededMaxSyncTime() {
        return DAYS.between(lastSync, Instant.now()) > MAX_SYNC_INTERVAL_DAYS;
    }

    public Map<String, List<String>> getCollaboratorsByRole() {
        return comics.stream()
                .map(Comic::getCollaborators)
                .flatMap(Collection::stream)
                .collect(toMap(
                            Collaborator::role,
                            c -> new ArrayList<>(Collections.singletonList(c.name())),
                            (list1, list2) -> {
                                list1.addAll(list2);
                                return list1;
                            } //method called to combine values when repeated key (same role)
                ));
    }


}
