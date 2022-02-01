package com.brujua.comics.service.marvel;

import com.brujua.comics.error.InvalidCharacterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GatewayTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    Gateway gateway;

    @Test
    void givenANonAlphanumericId_whenGettingCharacter_thenThrowsInvalidCharacterException() {
        assertThrows(InvalidCharacterException.class, () -> gateway.getCharacter("1234Test://"));
    }
}