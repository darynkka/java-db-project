package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "competition_participants")
@IdClass(CompetitionParticipantId.class)
public class CompetitionParticipant {

    @Id
    @ManyToOne
    @JoinColumn(name = "competition_id", referencedColumnName = "competition_id")
    private Competition competition;

    @Id
    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;
}
