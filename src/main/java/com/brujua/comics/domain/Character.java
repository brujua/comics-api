package com.brujua.comics.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.*;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toMap;

@Builder
@Getter
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Character {

    @Id
    private String id;

    private String name;
    private Instant lastSync;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Comic> comics;

    public Map<String, Set<String>> getCollaboratorsByRole() {
        return comics.stream()
                .map(Comic::getCollaborators)
                .flatMap(Collection::stream)
                .collect(toMap(
                            Collaborator::role,
                            c -> new HashSet<>(singleton(c.name())),
                            (set1, set2) -> {
                                //method called to combine values when repeated key (same role)
                                set1.addAll(set2);
                                return set1;
                            }
                ));
    }


}
