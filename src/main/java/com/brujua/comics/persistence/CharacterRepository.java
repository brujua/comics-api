package com.brujua.comics.persistence;

import com.brujua.comics.domain.Character;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CharacterRepository extends CrudRepository<Character, String> {
    @Override
    Optional<Character> findById(String id);
}
