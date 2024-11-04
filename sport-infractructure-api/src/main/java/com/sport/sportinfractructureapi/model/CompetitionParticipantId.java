package com.sport.sportinfractructureapi.model;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class CompetitionParticipantId implements Serializable {

    private Long competition;
    private Long athlete;

}
