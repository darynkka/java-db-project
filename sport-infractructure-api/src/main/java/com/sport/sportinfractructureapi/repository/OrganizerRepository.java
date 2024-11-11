package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query("SELECT organizer.organizerName, COUNT(competition.competitionId) " +
            "FROM Organizer organizer " +
            "JOIN Competition competition ON organizer.organizerId = competition.organizer.organizerId " +
            "WHERE competition.competitionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY organizer.organizerName")
    List<Object[]> findOrganizersAndCompetitionCountByDateRange(@P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);
    //список організаторів змагань та кількість змагань, які були ними
    //проведені протягом вказаного періоду


    @Query("SELECT o.organizerId FROM Organizer o WHERE o.organizerName = :name")
    Long findIdByName(@P("name") String name);

    List<Organizer> findAllByOrderByOrganizerId();
}