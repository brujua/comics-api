package com.brujua.comics.service.comics;

import com.brujua.comics.domain.Character;
import com.brujua.comics.domain.Comic;
import com.brujua.comics.repository.CharacterRepository;
import com.brujua.comics.service.marvel.Gateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    private static final int SYNC_INTERVAL_DAYS = 1;

    @Mock
    CharacterRepository repository;
    @Mock
    Gateway marvelGateway;

    CharacterService service;

    @BeforeEach
    void init() {
        service = new CharacterService(repository, marvelGateway, SYNC_INTERVAL_DAYS);
    }

    @Test
    void givenNonExistingIdInRepository_whenFindById_thenGetsItFromGateway() {
        String id = "test";
        Character character = Character.builder()
                .id(id)
                .name("spiderman")
                .lastSync(Instant.now())
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.empty());
        when(marvelGateway.getCharacter(id))
                .thenReturn(character);

        Character result = service.findById(id);

        assertThat(result).isEqualTo(character);
    }

    @SuppressWarnings("IntegerMultiplicationImplicitCastToLong")
    @Test
    void givenACharacterWithExceededMaxSync_whenFindById_thenSyncsWithGateway() {
        String id = "test";
        Character staleChar = Character.builder()
                .id(id)
                .name("spiderman")
                .lastSync(Instant.now().minus(2 * service.maxSyncIntervalDays, ChronoUnit.DAYS))
                .build();

        Character freshChar = Character.builder()
                .id(id)
                .name("spiderman")
                .lastSync(Instant.now())
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(staleChar));
        when(marvelGateway.getCharacter(id))
                .thenReturn(freshChar);

        Character result = service.findById(id);

        assertThat(result)
                .isNotEqualTo(staleChar)
                .isEqualTo(freshChar);
    }

    @Test
    void givenCharWith2ComicsAnd3companions_whenGettingCharsWithSharedComic_thenReturnsCompanions() {
        String charId = "spiderman";
        String companion1Id = "companion1";
        String companion2Id = "companion2";
        String companion3Id = "companion3";

        Comic comic1 = Comic.builder()
                .characterIds(List.of(charId, companion1Id, companion2Id))
                .build();
        Comic comic2 = Comic.builder()
                .characterIds(List.of(charId, companion2Id, companion3Id))
                .build();

        Character character = Character.builder()
                .id(charId)
                .comics(List.of(comic1, comic2))
                .lastSync(Instant.now())
                .build();

        Character companion1 = Character.builder()
                .id(companion1Id)
                .lastSync(Instant.now())
                .build();
        Character companion2 = Character.builder()
                .id(companion2Id)
                .lastSync(Instant.now())
                .build();
        Character companion3 = Character.builder()
                .id(companion3Id)
                .lastSync(Instant.now())
                .build();

        when(repository.findById(companion1Id))
                .thenReturn(Optional.of(companion1));
        when(repository.findById(companion2Id))
                .thenReturn(Optional.of(companion2));
        when(repository.findById(companion3Id))
                .thenReturn(Optional.of(companion3));

        List<Character> result = service.getCharactersWithSharedComic(character);

        assertThat(result).containsExactlyInAnyOrder(companion1, companion2, companion3);
    }

    @Test
    void givenACharWithAComic_whenGettingCharsWithSharedComic_thenItDoesNotIncludeTheCharacterGiven() {
        String charId = "spiderman";
        String companion1Id = "companion1";
        Comic comic = Comic.builder()
                .characterIds(List.of(charId, companion1Id))
                .build();
        Character character = Character.builder()
                .id(charId)
                .comics(List.of(comic))
                .lastSync(Instant.now())
                .build();

        Character companion1 = Character.builder()
                .id(companion1Id)
                .lastSync(Instant.now())
                .build();

        when(repository.findById(companion1Id))
                .thenReturn(Optional.of(companion1));

        List<Character> result = service.getCharactersWithSharedComic(character);

        assertThat(result).doesNotContain(character);
        assertThat(result).contains(companion1);
    }
}