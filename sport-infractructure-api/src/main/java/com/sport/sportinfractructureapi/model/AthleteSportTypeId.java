package com.sport.sportinfractructureapi.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class AthleteSportTypeId implements Serializable {

    private Long athlete;
    private Long sportType;
}
