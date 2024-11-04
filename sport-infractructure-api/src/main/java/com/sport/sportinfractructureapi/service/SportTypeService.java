package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.SportType;
import com.sport.sportinfractructureapi.repository.SportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SportTypeService {

    @Autowired
    private SportTypeRepository sportTypeRepository;

    public List<SportType> getAllSportTypes() {
        return sportTypeRepository.findAllByOrderBySportTypeId();
    }

    public Optional<SportType> getSportTypeById(Long id) {
        return sportTypeRepository.findById(id);
    }

    public SportType saveSportType(SportType sportType) {
        return sportTypeRepository.save(sportType);
    }

    public void deleteSportType(Long id) {
        sportTypeRepository.deleteById(id);
    }
}
