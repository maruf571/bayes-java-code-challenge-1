package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
