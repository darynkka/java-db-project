package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "athletes")
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "athlete_id")
    private Long athleteId;

    @Column(name = "athlete_name", nullable = false)
    private String athleteName;

    @Column(name = "athlete_rank")
    private String athleteRank;

    @ManyToOne
    @JoinColumn(name = "sport_club_id", referencedColumnName = "sportClub_id")
    private SportClub sportClub;
}
