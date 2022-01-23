package com.brujua.comics.service.marvel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ResponseWrapper {

    private static final ObjectMapper mapper = new ObjectMapper();


    @JsonProperty("code")
    int statusCode;

    @JsonProperty("data")
    ResultData data;

    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ResultData {
        int offset;
        int limit;
        int total;
        int count;
        List<Map<String, Object>> results;
    }

    public <T> T getSingleResult(Class<T> clazz) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        List <T> result = mapper.convertValue(Objects.requireNonNull(data).results, type);
        return result.stream()
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public <T> List<T> getResults(Class<T> clazz) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.convertValue(Objects.requireNonNull(data).results, type);
    }
}
