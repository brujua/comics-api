package com.brujua.comics.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Builder
@Getter
public class Comic {
    String id;
    String title;
    List<Collaborator> collaborators;
    List<String> characterIds;

    public Stream<String> characterIds(){
        return characterIds.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return id.equals(comic.id)
                && Objects.equals(title, comic.title)
                && collaborators.equals(comic.collaborators)
                && characterIds.equals(comic.characterIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, collaborators, characterIds);
    }
}
