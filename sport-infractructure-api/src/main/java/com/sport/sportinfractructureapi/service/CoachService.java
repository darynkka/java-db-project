package com.sport.sportinfractructureapi.service;
import com.sport.sportinfractructureapi.model.Athlete;
import com.sport.sportinfractructureapi.model.Coach;
import com.sport.sportinfractructureapi.repository.AthleteRepository;
import com.sport.sportinfractructureapi.repository.CoachRepository;
import com.sport.sportinfractructureapi.repository.SportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private SportTypeRepository sportTypeRepository;

    public List<Coach> getAllCoaches() {
        return coachRepository.findAllByOrderByCoachId();
    }

    public Optional<Coach> getCoachById(Long id) {
        return coachRepository.findById(id);
    }

    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    public void deleteCoach(Long id) {
        coachRepository.deleteById(id);
    }

    public List<Athlete> findAthletesByCoachName(String coachName) {
        Long coachId = coachRepository.findIdByName(coachName);
        return athleteRepository.findAthletesByCoach(coachId);
    }

    public List<Athlete> findAthletesByCoachNameAndRank(String coachName, String rank) {
        Long coachId = coachRepository.findIdByName(coachName);
        return athleteRepository.findAthletesByCoachAndRank(coachId, rank);
    }

    public List<Coach> findCoachesByAthleteAndSportType(String athleteName, String sportTypeName) {
        Long athleteId = athleteRepository.findIdByAthleteName(athleteName);
        Long sportTypeId = sportTypeRepository.findIdByName(sportTypeName);
        return coachRepository.findCoachesByAthleteAndSportType(athleteId, sportTypeId);
    }



}
