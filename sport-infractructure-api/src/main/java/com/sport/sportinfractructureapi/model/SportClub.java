package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "sport_clubs")
public class SportClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sportClub_id")
    private Long sportClubId;

    @Column(name = "sport_club_name", nullable = false)
    private String sportClubName;
}
