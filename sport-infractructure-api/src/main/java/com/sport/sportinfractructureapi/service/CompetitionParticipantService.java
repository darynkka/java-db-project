package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.CompetitionParticipant;
import com.sport.sportinfractructureapi.model.CompetitionParticipantId;
import com.sport.sportinfractructureapi.repository.CompetitionParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionParticipantService {

    @Autowired
    private CompetitionParticipantRepository competitionParticipantRepository;


    public List<CompetitionParticipant> getAllCompetitionParticipants() {
        return competitionParticipantRepository.findAllByOrderByCompetition();
    }

    public void saveCompetitionParticipant(CompetitionParticipant participant) {
        competitionParticipantRepository.save(participant);
    }

    public void deleteCompetitionParticipant(Long athleteId, Long competitionId) {
        CompetitionParticipantId id = new CompetitionParticipantId(competitionId, athleteId);
        competitionParticipantRepository.deleteById(id);
    }

    public void editCompetitionParticipant(CompetitionParticipant oldParticipant, CompetitionParticipant participant) {
        Optional<CompetitionParticipant> existingParticipant =
                competitionParticipantRepository
                        .findById(new CompetitionParticipantId(
                                oldParticipant.getCompetition().getCompetitionId(),
                                oldParticipant.getAthlete().getAthleteId()));
        if (existingParticipant.isPresent()) {
            competitionParticipantRepository.delete(existingParticipant.get());

            competitionParticipantRepository.save(participant);
        }
    }

    public boolean exists(CompetitionParticipant participant) {
        return
                competitionParticipantRepository
                        .findById(new CompetitionParticipantId(
                                participant.getCompetition().getCompetitionId(),
                                participant.getAthlete().getAthleteId()))
                        .isPresent();

    }
}
