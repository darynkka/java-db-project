package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "sport_facilities")
public class SportFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long facilityId;

    @Column(name = "facility_name", nullable = false)
    private String facilityName;

    @Column(name = "facility_address", nullable = false)
    private String facilityAddress;

    @Column(name = "facility_dimensions", nullable = false)
    private String facilityDimensions;

    @Column(name = "facility_type", nullable = false)
    private String facilityType;

    @Column(name = "stadium_capacity")
    private Integer stadiumCapacity;

    @Column(name = "cort_coating")
    private String cortCoating;

    @Column(name = "gym_equipment")
    private String gymEquipment;

    @Column(name = "playpen_ccs")
    private String playpenCcs;
}
