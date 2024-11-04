package com.sport.sportinfractructureapi.service;
import com.sport.sportinfractructureapi.model.Competition;
import com.sport.sportinfractructureapi.repository.CompetitionRepository;
import com.sport.sportinfractructureapi.repository.OrganizerRepository;
import com.sport.sportinfractructureapi.repository.SportFacilityRepository;
import com.sport.sportinfractructureapi.repository.SportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private SportFacilityRepository sportFacilityRepository;
    @Autowired
    private SportTypeRepository sportTypeRepository;

    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAllByOrderByCompetitionId();
    }

    public Optional<Competition> getCompetitionById(Long id) {
        return competitionRepository.findById(id);
    }

    public Competition saveCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }

    public void deleteCompetition(Long id) {
        competitionRepository.deleteById(id);
    }

    public List<Competition> getCompetitionsByDate(LocalDate startDate, LocalDate endDate) {
        return competitionRepository.findCompetitionsByDate(startDate, endDate);
    }

    public List<Competition> findCompetitionByOrganizerAndDate(String organizerName, LocalDate startDate, LocalDate endDate) {
        Long organizerId = organizerRepository.findIdByName(organizerName);
        return competitionRepository.findCompetitionsByOrganizerAndDate(organizerId, startDate, endDate);

    }

    public List<Competition> findCompetitionsByFacility(String facilityName) {
        Long facilityId = sportFacilityRepository.findIdByName(facilityName);
        return competitionRepository.findCompetitionsByFacility(facilityId);
    }

    public List<Competition> findCompetitionsByFacilityAndSportType(String facilityName, String sportType) {
        Long facilityId = sportFacilityRepository.findIdByName(facilityName);
        Long sportTypeId = sportTypeRepository.findIdByName(sportType);
        return competitionRepository.findCompetitionsByFacilityAndSportType(facilityId, sportTypeId);
    }

    public List<Object[]> getOrganizersAndCompetitionCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return organizerRepository.findOrganizersAndCompetitionCountByDateRange(startDate, endDate);
    }
}
