package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query("SELECT competition FROM Competition competition WHERE competition.competitionDate BETWEEN :startDate AND :endDate")
    List<Competition> findCompetitionsByDate(@P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);
    // знайти  перелік змагань проведених протягом вказаного періоду

    @Query("SELECT c FROM Competition c WHERE c.organizer.organizerId = :organizerId AND c.competitionDate BETWEEN :startDate AND :endDate")
    List<Competition> findCompetitionsByOrganizerAndDate(@P("organizerId") Long organizerId, @P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);
    // перелік змагань протягом вказаного періоду та за вказаним організатором


    @Query("SELECT c FROM Competition c WHERE c.sportFacility.facilityId = :facilityId")
    List<Competition> findCompetitionsByFacility(@P("facilityId") Long facilityId);
    // перелік змагань, проведених у вказаній спортивній споруді

    @Query("SELECT c FROM Competition c WHERE c.sportFacility.facilityId = :facilityId AND c.sportType.sportTypeId = :sportTypeId")
    List<Competition> findCompetitionsByFacilityAndSportType(@P("facilityId") Long facilityId, @P("sportTypeId") Long sportTypeId);
    // перелік змагань, проведених у вказаній спортивній споруді та з певного виду спорту

    @Query("SELECT c.competitionId FROM Competition c WHERE c.competitionName = :competitionName")
    Long findIdByName(@P("competitionName") String competitionName);

    List<Competition> findAllByOrderByCompetitionId();

}