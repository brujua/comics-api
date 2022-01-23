package com.brujua.comics.service.marvel;

import com.brujua.comics.domain.Collaborator;
import com.brujua.comics.domain.Comic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@ToString
public class MarvelComic {

    public record LightWeightCharacter(String id, String name){}

    String id;
    String title;
    String isbn;

    private List<Collaborator> collaborators;
    private List<LightWeightCharacter> characters;

    public Comic toDomain() {
        List<String> characterIds = characters.stream()
                .map(LightWeightCharacter::id)
                .toList();

        return Comic.builder()
                .id(id)
                .title(title)
                .collaborators(collaborators)
                .characterIds(characterIds)
                .build();
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("creators")
    private void unpackCollaborators(Map<String, Object> creators) {
        List<Map<String, String>> list = (List<Map<String, String>>) creators.getOrDefault("items", emptyList());
        this.collaborators = list.stream()
                .map(map -> new Collaborator(map.getOrDefault("name", ""), map.getOrDefault("role", "unknown")))
                .toList();
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("characters")
    private void unpackCharacters(Map<String, Object> characters) {
        List<Map<String, String>> list = (List<Map<String, String>>) characters.getOrDefault("items", emptyList());
        this.characters = list.stream()
                .map(this::deserializeCharacter)
                .toList();
    }

    private LightWeightCharacter deserializeCharacter(Map<String, String> map) {
        String resourceURI = map.getOrDefault("resourceURI", "");
        List<String> splitURI = Arrays.asList(StringUtils.split(resourceURI, '/'));
        String characterId = splitURI.get(splitURI.size() - 1);

        String name = map.getOrDefault("name", "");

        return new LightWeightCharacter(characterId, name);
    }
}
