package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.SportFacility;
import com.sport.sportinfractructureapi.repository.SportFacilityRepository;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ToString
@Service
public class SportFacilityService {

    @Autowired
    private SportFacilityRepository sportFacilityRepository;

    public List<SportFacility> getAllSportFacilities() {
        return sportFacilityRepository.findAllByOrderByFacilityId();
    }

    public Optional<SportFacility> getSportFacilityById(Long id) {
        return sportFacilityRepository.findById(id);
    }

    public SportFacility saveSportFacility(SportFacility sportFacility) {
        return sportFacilityRepository.save(sportFacility);
    }

    public void deleteSportFacility(Long id) {
        sportFacilityRepository.deleteById(id);
    }

    public List<SportFacility> findFacilitiesByType(String facilityType) {
        return sportFacilityRepository.findFacilitiesByType(facilityType);
    }

    public List<SportFacility> getStadiumsByCapacity(int minCapacity) {
        return sportFacilityRepository.findStadiumsByCapacity(minCapacity);
    }

    public List<SportFacility> getPlaypensByCCS(String playpenCCS) {
        return sportFacilityRepository.findPlaypensByCCS(playpenCCS);
    }

    public List<SportFacility> getGymsByEquipment(String gymEquipment) {
        return sportFacilityRepository.findGymsByEquipment(gymEquipment);
    }

    public List<SportFacility> getCortsByCoating(String cortCoating) {
        return sportFacilityRepository.findCortsByCoating(cortCoating);
    }

    public List<Object[]> getFacilitiesAndCompetitionDatesByDateRange(LocalDate startDate, LocalDate endDate) {
        return sportFacilityRepository.findFacilitiesAndCompetitionDatesByDateRange(startDate, endDate);
    }
}
