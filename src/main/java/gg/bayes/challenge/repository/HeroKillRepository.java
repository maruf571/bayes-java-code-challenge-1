package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.BaseKill;
import gg.bayes.challenge.rest.model.HeroKills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroKillRepository extends JpaRepository<BaseKill, Long> {

    @Query( "SELECT new gg.bayes.challenge.rest.model.HeroKills(k.hero, count(k)) " +
            "FROM BaseKill k " +
            "where k.match.id = ?1 group by k.hero ")
    List<HeroKills> getHeroKills(Long matchId);

}
