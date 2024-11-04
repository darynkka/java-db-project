package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.AthleteCoach;
import com.sport.sportinfractructureapi.model.AthleteCoachId;
import com.sport.sportinfractructureapi.model.CompetitionParticipant;
import com.sport.sportinfractructureapi.model.CompetitionParticipantId;
import com.sport.sportinfractructureapi.repository.AthleteCoachRepository;
import com.sport.sportinfractructureapi.repository.CompetitionParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteCoachService {
    @Autowired
    private AthleteCoachRepository athleteCoachRepository;


    public List<AthleteCoach> getAllAthletesCoaches() {
        return athleteCoachRepository.findAllByOrderByAthlete();
    }

    public void saveAthleteCoach(AthleteCoach athleteCoach) {
        athleteCoachRepository.save(athleteCoach);
    }

    public void deleteAthleteCoach(Long athleteId, Long coachId, Long sportTypeId) {
        AthleteCoachId id = new AthleteCoachId(athleteId, coachId, sportTypeId);
        athleteCoachRepository.deleteById(id);
    }

    public void editAthleteCoach(AthleteCoach oldValue, AthleteCoach newValue) {
        Optional<AthleteCoach> existingAthleteCoach =
                athleteCoachRepository
                        .findById(new AthleteCoachId(
                                oldValue.getAthlete().getAthleteId(),
                                oldValue.getCoach().getCoachId(), oldValue.getSportType().getSportTypeId()));
        if (existingAthleteCoach.isPresent()) {
            athleteCoachRepository.delete(existingAthleteCoach.get());

            athleteCoachRepository.save(newValue);
        }
    }

    public boolean exists(AthleteCoach athleteCoach) {
        return
                athleteCoachRepository
                        .findById(new AthleteCoachId(
                                athleteCoach.getAthlete().getAthleteId(),
                                athleteCoach.getCoach().getCoachId(),
                                athleteCoach.getSportType().getSportTypeId()))
                        .isPresent();

    }
}
