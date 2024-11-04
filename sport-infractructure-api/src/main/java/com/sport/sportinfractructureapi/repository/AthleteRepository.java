package com.sport.sportinfractructureapi.repository;

import com.sport.sportinfractructureapi.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    @Query("SELECT athleteSportType.athlete FROM AthleteSportType athleteSportType " +
            "JOIN athleteSportType.sportType sportType " +
            "WHERE sportType.sportTypeId = :sportTypeId")
    List<Athlete> findAthletesBySportType(@P("sportTypeId") Long sportTypeId);
    // список спортсменів, що займаються певним видом спорту

    @Query("SELECT DISTINCT a FROM Athlete a " +
            "JOIN AthleteSportType ast ON a.athleteId = ast.athlete.athleteId " +
            "JOIN SportType st ON ast.sportType.sportTypeId = st.sportTypeId " +
            "WHERE st.sportTypeId = :sportTypeId " +
            "AND a.athleteRank = :rank")
    List<Athlete> findAthletesBySportTypeAndRank(@P("sportTypeId") Long sportTypeId, @P("rank") String rank);
    // список спортсменів, що займаються певним вс та не нижче певного розряду

    @Query("SELECT DISTINCT a FROM Athlete a " +
            "JOIN AthleteSportType ast ON a.athleteId = ast.athlete.athleteId " +
            "JOIN SportType st ON ast.sportType.sportTypeId = st.sportTypeId " +
            "WHERE st.sportName IN :sportTypes " +
            "GROUP BY a.athleteId " +
            "HAVING COUNT(DISTINCT st.sportName) = :sportTypesCount")
    List<Athlete> findAthletesByMultipleSportTypes(@P("sportTypes") List<String> sportTypes,
                                                   @P("sportTypesCount") Long sportTypesCount);
    // список спортсменів, які займаються декількома видами спорту

    @Query("SELECT athlete FROM Athlete athlete " +
            "LEFT JOIN CompetitionParticipant competitionParticipant ON athlete.athleteId = competitionParticipant.athlete.athleteId " +
            "LEFT JOIN Competition competition ON competitionParticipant.competition.competitionId = competition.competitionId " +
            "WHERE competition.competitionDate IS NULL OR competition.competitionDate NOT BETWEEN :startDate AND :endDate")
    List<Athlete> findAthletesNotInCompetitionsByDate(@P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);
    // список спортсменів, що не брали участь в жодному змаганні

    @Query("SELECT ac.athlete FROM AthleteCoach ac " +
            "WHERE ac.coach.coachId = :coachId")
    List<Athlete> findAthletesByCoach(@P("coachId") Long coachId);
    //список спортсменів, які тренуються у вказаного тренера загалом

    @Query("SELECT ac.athlete FROM AthleteCoach ac " +
            "WHERE ac.coach.coachId = :coachId AND ac.athlete.athleteRank = :rank")
    List<Athlete> findAthletesByCoachAndRank(@P("coachId") Long coachId, @P("rank") String rank);
    // список спортсменів, які тренуються у вказаного тренера та певного розряду

    @Query("SELECT a.athleteId FROM Athlete a WHERE a.athleteName = :athleteName")
    Long findIdByAthleteName(@P("athleteName") String athleteName);

    List<Athlete> findAllByOrderByAthleteId();

}