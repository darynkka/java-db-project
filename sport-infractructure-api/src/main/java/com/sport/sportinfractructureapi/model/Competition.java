package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name = "competitions")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_id")
    private Long competitionId;

    @Column(name = "competition_name", nullable = false)
    private String competitionName;

    @ManyToOne
    @JoinColumn(name = "sport_type_id", referencedColumnName = "sport_type_id")
    private SportType sportType;

    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "organizer_id")
    private Organizer organizer;

    @Column(name = "competition_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date competitionDate;

    @ManyToOne
    @JoinColumn(name = "sport_facility_id", referencedColumnName = "facility_id")
    private SportFacility sportFacility;
}
