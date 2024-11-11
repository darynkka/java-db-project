package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("SELECT awarding FROM Award awarding WHERE awarding.competition.competitionId = :competitionId")
    List<Award> getAwardsByCompetition(@P("competitionId") Long competitionId);
    // список призерів вказаного змагання
    List<Award> findAllByOrderByAwardId();
}