package com.brujua.comics.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Builder
@Getter
public class Comic {
    String title;
    String id;
    List<Collaborator> collaborators;
    List<String> characterIds;

    public Stream<String> characterIds(){
        return characterIds.stream();
    }
}
