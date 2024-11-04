package com.sport.sportinfractructureapi.repository;

import com.sport.sportinfractructureapi.model.Athlete;
import com.sport.sportinfractructureapi.model.AthleteCoach;
import com.sport.sportinfractructureapi.model.AthleteCoachId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteCoachRepository extends JpaRepository<AthleteCoach, AthleteCoachId> {
    void deleteById(AthleteCoachId athleteCoachId);

    List<AthleteCoach> findAllByOrderByAthlete();
}
