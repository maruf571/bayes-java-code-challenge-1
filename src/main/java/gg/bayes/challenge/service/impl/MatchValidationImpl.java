package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.exception.DotaException;
import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.service.MatchValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchValidationImpl implements MatchValidation {

    private final MatchRepository matchRepository;

    @Override
    public MatchValidation validateMatch(Long matchId) {
        matchRepository.findById(matchId).orElseThrow(
                () -> new DotaException("Match not exist with the id: " + matchId, HttpStatus.NOT_FOUND)
        );
        return this;
    }

    @Override
    public void validateHeroName(String heroName) {
        // In future we will query from database to validate hero is exist or not
        if(heroName == null || heroName.trim().length() < 1) {
            throw new DotaException("Hero name is null or not found", HttpStatus.NOT_FOUND);
        }
    }
}
