package com.sport.sportinfractructureapi.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AthleteCoachId implements Serializable { // для складеного ключа існує

    private Long athlete;
    private Long coach;
    private Long sportType;

}
