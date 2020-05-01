package gg.bayes.challenge.service;

public interface MatchValidation {

    MatchValidation validateMatch(Long matchId);

    void validateHeroName(String heroName);
}
