package com.brujua.comics.service.comics;

import com.brujua.comics.domain.Character;
import com.brujua.comics.persistence.CharacterRepository;
import com.brujua.comics.service.marvel.Gateway;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final  CharacterRepository repository;
    private final Gateway marvelGateway;

    public CharacterService(CharacterRepository repository, Gateway marvelGateway) {
        this.repository = repository;
        this.marvelGateway = marvelGateway;
    }

    /**
     * @throws com.brujua.comics.error.InvalidCharacterException if the charId is invalid
     */
    public Character findById(String charId) {
        Character character = repository.findById(charId)
                .orElseGet(() -> getCharacterFromMarvel(charId));

        if (character.exceededMaxSyncTime()) {
            character = getCharacterFromMarvel(charId);
        }

        return character;
    }

    private Character getCharacterFromMarvel(String charId) {
        Character character = marvelGateway.getCharacter(charId);
        repository.save(character);
        return character;
    }
}
