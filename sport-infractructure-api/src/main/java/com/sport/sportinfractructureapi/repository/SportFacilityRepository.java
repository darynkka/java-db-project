package com.sport.sportinfractructureapi.repository;

import com.sport.sportinfractructureapi.model.Athlete;
import com.sport.sportinfractructureapi.model.SportFacility;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface SportFacilityRepository extends JpaRepository<SportFacility, Long> {

    @Query("SELECT sportFacility FROM SportFacility sportFacility WHERE sportFacility.facilityType = :facilityType")
    List<SportFacility> findFacilitiesByType(@P("facilityType") String facilityType);
    // перелік споруд вказаного типу

    @Query("SELECT sportFacility FROM SportFacility sportFacility WHERE sportFacility.stadiumCapacity >= :minCapacity")
    List<SportFacility> findStadiumsByCapacity(@P("minCapacity") int minCapacity);
    // за місткістю (стадіони)

    @Query("SELECT sportFacility FROM SportFacility sportFacility WHERE sportFacility.playpenCcs = :playpenCCS")
    List<SportFacility> findPlaypensByCCS(@P("playpenCCS") String playpenCCS);
    // клімат контроль (манежі)

    @Query("SELECT sportFacility FROM SportFacility sportFacility WHERE sportFacility.gymEquipment LIKE %:gymEquipment%")
    List<SportFacility> findGymsByEquipment(@P("gymEquipment") String gymEquipment);
    // обладнання (спортзал)

    @Query("SELECT sportFacility FROM SportFacility sportFacility WHERE sportFacility.cortCoating = :cortCoating")
    List<SportFacility> findCortsByCoating(@P("cortCoating") String cortCoating);
    // тип покриття (корт)

    @Query("SELECT sportFacility FROM SportFacility sportFacility " +
            "JOIN Competition competition ON sportFacility.facilityId = competition.sportFacility.facilityId " +
            "WHERE competition.sportType.sportTypeId = :sportTypeId")
    List<SportFacility> findFacilitiesBySportType(@P("sportTypeId") Long sportTypeId);
    //за типом споруди і видом спорту


    @Query("SELECT sf.facilityName, sf.facilityAddress, c.competitionDate " +
            "FROM SportFacility sf " +
            "JOIN Competition c ON sf = c.sportFacility " +
            "WHERE c.competitionDate BETWEEN :startDate AND :endDate")
    List<Object[]> findFacilitiesAndCompetitionDatesByDateRange(@P("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);
    //перелік спортивних споруд та дати проведення на них змагань протягом певного періоду

    @Query("SELECT s.facilityId FROM SportFacility s WHERE s.facilityName = :name")
    Long findIdByName(@P("name") String name);

    List<SportFacility> findAllByOrderByFacilityId();
}
