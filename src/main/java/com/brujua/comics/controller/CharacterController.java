package com.brujua.comics.controller;

import com.brujua.comics.controller.dto.CharactersResponse;
import com.brujua.comics.controller.dto.CollaboratorsResponse;
import com.brujua.comics.domain.Character;
import com.brujua.comics.service.comics.CharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CharacterController {
    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/colaborators/{characterId}")  // endpoint could be in a CollaboratorsController, scope doesn't justify it
    CollaboratorsResponse getCollaborators(@PathVariable String charId) {
        Character character = characterService.findById(charId);
        return new CollaboratorsResponse(character.getLastSync(), character.getCollaboratorsByRole());
    }

    @GetMapping("/characters/{characterId}")
    CharactersResponse getSharedCast(@PathVariable String charId) {
        Character character = characterService.findById(charId);
        List<Character> characters = characterService.getCharactersWithSharedComic(character);
        return new CharactersResponse(character.getLastSync(), characters);
    }
}
