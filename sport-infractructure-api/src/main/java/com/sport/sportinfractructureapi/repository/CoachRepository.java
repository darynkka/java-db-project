package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    @Query("SELECT c.coachId FROM Coach c WHERE c.coachName = :coachName")
    Long findIdByName(@P("coachName") String coachName);


    @Query("SELECT ac.coach FROM AthleteCoach ac " +
            "WHERE ac.athlete.athleteId = :athleteId AND ac.sportType.sportTypeId = :sportTypeId")
    List<Coach> findCoachesByAthleteAndSportType(@P("athleteId") Long athleteId, @P("sportTypeId") Long sportTypeId);

    List<Coach> findAllByOrderByCoachId();

}