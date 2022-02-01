package com.brujua.comics.repository;

import com.brujua.comics.domain.Character;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CharacterRepository extends CrudRepository<Character, String> {
    @Override
    @NonNull
    Optional<Character> findById(@NonNull String id);
}
