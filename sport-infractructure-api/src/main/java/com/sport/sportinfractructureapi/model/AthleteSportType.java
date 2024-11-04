package com.sport.sportinfractructureapi.model;


import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "athlete_sport_types")
@IdClass(AthleteSportTypeId.class)
public class AthleteSportType {

    @Id
    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;

    @Id
    @ManyToOne
    @JoinColumn(name = "sport_type_id", referencedColumnName = "sport_type_id")
    private SportType sportType;
}
