package com.sport.sportinfractructureapi.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "awards")
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long awardId;

    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;

    @Column(name = "athlete_place", nullable = false)
    private Integer athletePlace;

    @ManyToOne
    @JoinColumn(name = "competition_id", referencedColumnName = "competition_id")
    private Competition competition;

    @Column(name = "award_type", nullable = false)
    private String awardType;
}
