package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.AthleteCoach;
import com.sport.sportinfractructureapi.model.AthleteCoachId;
import com.sport.sportinfractructureapi.model.AthleteSportType;
import com.sport.sportinfractructureapi.model.AthleteSportTypeId;
import com.sport.sportinfractructureapi.repository.AthleteCoachRepository;
import com.sport.sportinfractructureapi.repository.AthleteSportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteSportTypeService {
    @Autowired
    private AthleteSportTypeRepository athleteSportTypeRepository;


    public List<AthleteSportType> getAllAthletesSportTypes() {
        return athleteSportTypeRepository.findAllByOrderByAthlete();
    }

    public void saveAthleteSportType(AthleteSportType athleteSportType) {
        athleteSportTypeRepository.save(athleteSportType);
    }

    public void deleteAthleteSportType(Long athleteId, Long sportTypeId) {
        AthleteSportTypeId id = new AthleteSportTypeId(athleteId, sportTypeId);
        athleteSportTypeRepository.deleteById(id);
    }

    public void editAthleteSportType(AthleteSportType oldValue, AthleteSportType newValue) {
        Optional<AthleteSportType> existingAthleteSportType =
                athleteSportTypeRepository
                        .findById(new AthleteSportTypeId(
                                oldValue.getAthlete().getAthleteId(),
                                oldValue.getSportType().getSportTypeId()));
        if (existingAthleteSportType.isPresent()) {
            athleteSportTypeRepository.delete(existingAthleteSportType.get());

            athleteSportTypeRepository.save(newValue);
        }
    }

    public boolean exists(AthleteSportType athleteSportType) {
        return
                athleteSportTypeRepository
                        .findById(new AthleteSportTypeId(
                                athleteSportType.getAthlete().getAthleteId(),
                                athleteSportType.getSportType().getSportTypeId()))
                        .isPresent();

    }
}
