package com.sport.sportinfractructureapi.repository;
import com.sport.sportinfractructureapi.model.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportTypeRepository extends JpaRepository<SportType, Long> {

    @Query("SELECT s.sportTypeId FROM SportType s WHERE s.sportName = :name")
    Long findIdByName(@P("name") String name);

    List<SportType> findAllByOrderBySportTypeId();

}