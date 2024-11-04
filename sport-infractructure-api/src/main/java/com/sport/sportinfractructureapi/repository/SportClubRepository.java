package com.sport.sportinfractructureapi.repository;

import com.sport.sportinfractructureapi.model.Athlete;
import com.sport.sportinfractructureapi.model.SportClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SportClubRepository extends JpaRepository<SportClub, Long> {

    @Query("SELECT sportClub.sportClubName, COUNT(DISTINCT athlete.athleteId) " +
            "FROM SportClub sportClub " +
            "INNER JOIN Athlete athlete ON sportClub.sportClubId = athlete.sportClub.sportClubId " +
            "INNER JOIN CompetitionParticipant competitionParticipants ON athlete.athleteId = competitionParticipants.athlete.athleteId " +
            "INNER JOIN Competition competition ON competitionParticipants.competition.competitionId = competition.competitionId " +
            "WHERE competition.competitionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY sportClub.sportClubName")
    List<Object[]> findSportClubsAndAthleteCountByCompetitionsDate(@P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);

    //перелік спортивних клубів та кількість спортсменів цих клубів, які брали участь у
    //  спортивних змаганнях протягом періоду

    List<SportClub> findAllByOrderBySportClubId();
}