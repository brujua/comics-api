package com.brujua.comics.service.comics;

import com.brujua.comics.domain.Character;
import com.brujua.comics.repository.CharacterRepository;
import com.brujua.comics.service.marvel.Gateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
}