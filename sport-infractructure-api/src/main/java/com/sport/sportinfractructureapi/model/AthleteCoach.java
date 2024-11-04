package com.sport.sportinfractructureapi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "athlete_coaches")
@IdClass(AthleteCoachId.class)
public class AthleteCoach {

    @Id
    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;

    @Id
    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "coach_id")
    private Coach coach;

    @Id
    @ManyToOne
    @JoinColumn(name = "sport_type_id", referencedColumnName = "sport_type_id")
    private SportType sportType;
}
