package com.brujua.comics.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class CharacterTest {
    @Test
    void given3comicsMultipleCollaborators_whenGettingByRole_thenGroupsThemCorrectly() {

        Comic comic1 = Comic.builder()
                .id("comic1")
                .collaborators(List.of(
                                new Collaborator("John", "writer"),
                                new Collaborator("Julia", "ink"),
                                new Collaborator("Paul", "help")
                        )
                ).build();
        Comic comic2 = Comic.builder()
                .id("comic2")
                .collaborators(List.of(
                                new Collaborator("Paul", "writer"),
                                new Collaborator("Maria", "writer")
                        )
                ).build();
        Comic comic3 = Comic.builder()
                .id("comic3")
                .collaborators(List.of(
                                new Collaborator("Nicholas", "ink")
                        )
                ).build();

        Character character = Character.builder()
                .id("test")
                .comics(List.of(comic1, comic2, comic3))
                .build();

        Map<String, Set<String>> collabsByRole = character.getCollaboratorsByRole();

        Set<String> writers = collabsByRole.get("writer");
        Set<String> inkers = collabsByRole.get("ink");
        Set<String> helpers = collabsByRole.get("help");

        assertThat(writers).containsExactlyInAnyOrder("John", "Paul", "Maria");
        assertThat(inkers).containsExactlyInAnyOrder("Julia", "Nicholas");
        assertThat(helpers).containsExactly("Paul");
    }
}