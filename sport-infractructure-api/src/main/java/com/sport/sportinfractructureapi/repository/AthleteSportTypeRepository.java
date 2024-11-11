package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.AthleteSportType;
import com.sport.sportinfractructureapi.model.AthleteSportTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteSportTypeRepository extends JpaRepository<AthleteSportType, AthleteSportTypeId> {
    void deleteById(AthleteSportTypeId athleteSportTypeId);
    List<AthleteSportType> findAllByOrderByAthlete();
}
