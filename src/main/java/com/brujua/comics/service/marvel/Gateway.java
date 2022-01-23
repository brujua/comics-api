package com.brujua.comics.service.marvel;

import com.brujua.comics.domain.Character;
import com.brujua.comics.domain.Comic;
import com.brujua.comics.error.InvalidCharacterException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class Gateway {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    //TODO externalize configuration env variables
    private static final String publicKey = "";
    private static final String privateKey = "";
    private static final String baseUrl = "http://gateway.marvel.com/v1/public";

    public Gateway() {
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Character getCharacter(String charId) {
        MarvelCharacter marvelCharacter = retrieveCharacter(charId);

        /*marvelCharacter.comics.stream()
                .map(LightWeightComic::id)
                .map()*/

        List<Comic> comics = Collections.emptyList(); //TODO
        
        return Character.builder()
                .id(marvelCharacter.getId())
                .name(marvelCharacter.getName())
                .lastSync(Instant.now())
                .comics(comics)
                .build();
    }

    private MarvelCharacter retrieveCharacter(String charId) {
        if (!StringUtils.isAlphanumeric(charId)) {
            throw new InvalidCharacterException();
        }

        ResponseWrapper response = callMarvelApi("/characters/" + charId);
        return response.getSingleResult(MarvelCharacter.class);
    }

    private MarvelComic retrieveComic(String comicId) {
        if (!StringUtils.isAlphanumeric(comicId)) {
            throw new IllegalArgumentException();
        }

        return callMarvelApi("/comics/" + comicId)
                .getSingleResult(MarvelComic.class);
    }

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

        return response.getBody();
    }

    public static void main(String[] args) {
        Gateway gateway = new Gateway();
        MarvelCharacter character = gateway.retrieveCharacter("1010911");
        System.out.println(character.getId());
        System.out.println(character.getName());
        System.out.println(character.getComics());

        MarvelComic comic = gateway.retrieveComic(character.getComics().stream().findFirst().map(MarvelCharacter.LightWeightComic::id).orElseThrow());
        System.out.println(comic.getId());
        System.out.println(comic.getTitle());
        System.out.println(comic.getCharacters());
    }
}
