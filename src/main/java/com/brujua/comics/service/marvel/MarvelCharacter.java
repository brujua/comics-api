package com.brujua.comics.service.marvel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelCharacter {

    public record LightWeightComic(String id, String name) {}

    String id;
    String name;
    List<LightWeightComic> comics;

    @SuppressWarnings("unchecked")
    @JsonProperty("comics")
    private void unpackCharacters(Map<String, Object> characters) {
        List<Map<String, String>> list = (List<Map<String, String>>) characters.getOrDefault("items", emptyList());
        this.comics = list.stream()
                .map(this::deserializeCharacter)
                .toList();
    }

    private LightWeightComic deserializeCharacter(Map<String, String> map) {
        String resourceURI = map.getOrDefault("resourceURI", "");
        List<String> splitURI = Arrays.asList(StringUtils.split(resourceURI, '/'));
        String comicId = splitURI.get(splitURI.size() - 1);

        String name = map.getOrDefault("name", "");

        return new LightWeightComic(comicId, name);
    }
}
