package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.SportClub;
import com.sport.sportinfractructureapi.repository.SportClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SportClubService {

    @Autowired
    private SportClubRepository sportClubRepository;

    public List<SportClub> getAllSportClubs() {
        return sportClubRepository.findAllByOrderBySportClubId();
    }

    public Optional<SportClub> getSportClubById(Long id) {
        return sportClubRepository.findById(id);
    }

    public SportClub saveSportClub(SportClub sportClub) {
        return sportClubRepository.save(sportClub);
    }

    public void deleteSportClub(Long id) {
        sportClubRepository.deleteById(id);
    }

    public List<Object[]> getSportClubsAndAthleteCountByCompetitionsDate(LocalDate startDate, LocalDate endDate) {
        return sportClubRepository.findSportClubsAndAthleteCountByCompetitionsDate(startDate, endDate);
    }
}
