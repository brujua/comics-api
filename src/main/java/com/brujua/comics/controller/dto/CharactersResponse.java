package com.brujua.comics.controller.dto;

import com.brujua.comics.domain.Character;
import com.brujua.comics.domain.Comic;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@SuppressWarnings({"java:S116","java:S117"})
public class CharactersResponse {
    private final Instant last_sync;
    private final List<CharacterDTO> characters;

    public CharactersResponse(Instant last_sync, List<Character> characters) {
        this.last_sync = last_sync;
        this.characters = characters.stream().map(CharacterDTO::new).toList();
    }

    @Getter
    private static class CharacterDTO {
        String character;
        List<String> comics;

        public CharacterDTO(Character character) {
            this.character = character.getName();
            comics = character.getComics().stream().map(Comic::getTitle).toList();
        }
    }
}
