package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.CompetitionParticipant;
import com.sport.sportinfractructureapi.model.CompetitionParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, CompetitionParticipantId> {
    void deleteById(CompetitionParticipantId id);
    List<CompetitionParticipant> findAllByOrderByCompetition();
}