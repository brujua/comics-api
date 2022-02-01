package com.brujua.comics.service.comics;

import com.brujua.comics.domain.Character;
import com.brujua.comics.domain.Comic;
import com.brujua.comics.repository.CharacterRepository;
import com.brujua.comics.service.marvel.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CharacterService {

    private static final Logger logger = LoggerFactory.getLogger(CharacterService.class);

    final int maxSyncIntervalDays;

    private final  CharacterRepository repository;
    private final Gateway marvelGateway;

    @Autowired
    public CharacterService(CharacterRepository repository, Gateway marvelGateway, @Value("${comics.max_sync_interval_days}") int maxSync) {
        this.repository = repository;
        this.marvelGateway = marvelGateway;
        this.maxSyncIntervalDays = maxSync;
    }

    public Character findById(String charId) {
        Character character = repository.findById(charId)
                .orElseGet(() -> getCharacterFromMarvel(charId));

        if (exceededMaxSyncTime(character.getLastSync())) {
            logger.info("Exceeded max sync time of {}", maxSyncIntervalDays);
            character = getCharacterFromMarvel(charId);
        }

        return character;
    }

    private Character getCharacterFromMarvel(String charId) {
        logger.info("Syncing character {} with marvel", charId);
        Character character = marvelGateway.getCharacter(charId);
        repository.save(character);
        return character;
    }

    public List<Character> getCharactersWithSharedComic(Character character) {
        Set<String> ids = character.getComics().stream()
                .flatMap(Comic::characterIds)
                .collect(Collectors.toSet());

        return ids.stream()
                .parallel()
                .filter(id -> !character.getId().equals(id))
                .map(this::findById) //TODO could be done in a single query
                .toList();
    }

    private boolean exceededMaxSyncTime(Instant lastSync) {
        return DAYS.between(lastSync, Instant.now()) > maxSyncIntervalDays;
    }
}
