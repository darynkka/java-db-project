package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.Athlete;
import com.sport.sportinfractructureapi.repository.AthleteRepository;
import com.sport.sportinfractructureapi.repository.SportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final SportTypeRepository sportTypeRepository;

    @Autowired
    public AthleteService(AthleteRepository athleteRepository, SportTypeRepository sportTypeRepository) {
        this.athleteRepository = athleteRepository;
        this.sportTypeRepository = sportTypeRepository;
    }

    public List<Athlete> getAllAthletes() {
      return athleteRepository.findAllByOrderByAthleteId();
    }

    public Optional<Athlete> getAthleteById(Long id) {
        return athleteRepository.findById(id);
    }

    public Athlete saveAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteAthlete(Long id) {

        athleteRepository.deleteById(id);
    }

    public List<Athlete> findAthletesBySportTypeName(String sportTypeName) {
        Long sportTypeId = sportTypeRepository.findIdByName(sportTypeName);
        return athleteRepository.findAthletesBySportType(sportTypeId);
    }

    public List<Athlete> findAthletesBySportTypeNameAndRank(String sportTypeName, String rank) {
        Long sportTypeId = sportTypeRepository.findIdByName(sportTypeName);
        return athleteRepository.findAthletesBySportTypeAndRank(sportTypeId, rank);
    }

    public List<Athlete> findAthletesByMultipleSportTypes(String sportTypesInput) {
        List<String> sportTypes = Arrays.stream(sportTypesInput.split("\\s*,\\s*"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return athleteRepository.findAthletesByMultipleSportTypes(sportTypes, (long) sportTypes.size());
    }

    public List<Athlete> findAthletesNotInCompetitionsByDate(LocalDate startDate, LocalDate endDate) {
        return athleteRepository.findAthletesNotInCompetitionsByDate(startDate, endDate);
    }
}