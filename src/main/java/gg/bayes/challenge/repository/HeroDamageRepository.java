package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.BaseDamage;
import gg.bayes.challenge.rest.model.HeroDamages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroDamageRepository extends JpaRepository<BaseDamage, Long> {

    @Query("select new gg.bayes.challenge.rest.model.HeroDamages(hd.target, count(hd) , sum(hd.damage)  ) " +
            "from BaseDamage as hd " +
            "where hd.match.id = ?1 and hd.hero=?2 group by hd.target ")
    List<HeroDamages> damage(Long matchId, String heroName);
}
