package com.sport.sportinfractructureapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "sport_types")
public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sport_type_id")
    private Long sportTypeId;

    @Column(name = "sport_name", nullable = false)
    private String sportName;
}
