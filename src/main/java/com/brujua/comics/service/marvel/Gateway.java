package com.brujua.comics.service.marvel;

import com.brujua.comics.domain.Character;
import com.brujua.comics.domain.Comic;
import com.brujua.comics.error.InvalidCharacterException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * <p>Gateway that represents the Marvel API and allows querying its resources</p>
 */
@Service
public class Gateway {

    private final RestTemplate restTemplate;

    @Value("${marvel.publicKey}")
    private String publicKey;
    @Value("${marvel.privateKey}")
    private String privateKey;
    @Value("${marvel.baseurl}")
    private String baseUrl;

    @Autowired
    public Gateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @throws com.brujua.comics.error.InvalidCharacterException if the charId is invalid
     */
    public Character getCharacter(String charId) {
        try {
            MarvelCharacter marvelCharacter = retrieveCharacter(charId);

            List<Comic> comics = getComicsByCharacter(charId);

            return Character.builder()
                    .id(marvelCharacter.getId())
                    .name(marvelCharacter.getName())
                    .lastSync(Instant.now())
                    .comics(comics)
                    .build();

        } catch(HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new InvalidCharacterException(charId);
            }
            throw e;
        }
    }

    private List<Comic> getComicsByCharacter(String characterId) {
        if (!StringUtils.isAlphanumeric(characterId)) {
            throw new InvalidCharacterException(characterId);
        }

        return callMarvelApi("/characters/" + characterId + "/comics")
                .getResults(MarvelComic.class).stream()
                .map(MarvelComic::toDomain)
                .toList();
    }

    private MarvelCharacter retrieveCharacter(String characterId) {
        if (!StringUtils.isAlphanumeric(characterId)) {
            throw new InvalidCharacterException(characterId);
        }

        return callMarvelApi("/characters/" + characterId)
                .getSingleResult(MarvelCharacter.class);
    }

    @NonNull
    private ResponseWrapper callMarvelApi(String path) {
        long ts = System.currentTimeMillis();
        String hash = DigestUtils.md5Hex(ts + privateKey + publicKey);

        String uriString = UriComponentsBuilder.fromHttpUrl(baseUrl + path)
                .queryParam("ts", ts)
                .queryParam("hash", hash)
                .queryParam("apikey", publicKey)
                .encode()
                .toUriString();


        ResponseEntity<ResponseWrapper> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                null,
                ResponseWrapper.class
        );
        return requireNonNull(response.getBody());
    }
}
