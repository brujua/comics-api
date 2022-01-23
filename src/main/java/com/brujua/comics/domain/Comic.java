package com.brujua.comics.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Comic {
    String name;
    List<Collaborator> collaborators;
    List<String> charactersIds;
}
