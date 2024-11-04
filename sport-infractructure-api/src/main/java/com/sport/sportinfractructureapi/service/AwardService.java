package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.Award;
import com.sport.sportinfractructureapi.repository.AwardRepository;
import com.sport.sportinfractructureapi.repository.CompetitionRepository;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@ToString
@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

    public List<Award> getAllAwards() {
        return awardRepository.findAllByOrderByAwardId();
    }

    public Optional<Award> getAwardById(Long id) {
        return awardRepository.findById(id);
    }

    public Award saveAward(Award award) {
        return awardRepository.save(award);
    }

    public void deleteAward(Long id) {
        awardRepository.deleteById(id);
    }

    public List<Award> getAwardsByCompetition(String competitionName) {
        Long competitionId = competitionRepository.findIdByName(competitionName);
        return awardRepository.getAwardsByCompetition(competitionId);
    }
}
