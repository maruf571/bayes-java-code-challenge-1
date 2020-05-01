package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.HeroDamage;
import gg.bayes.challenge.rest.model.HeroDamages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroDamageRepository extends JpaRepository<HeroDamage, Long> {

    @Query("select new gg.bayes.challenge.rest.model.HeroDamages(hd.target, count(hd) , sum(hd.damage)  ) " +
            "from HeroDamage as hd " +
            "where hd.match.id = ?1 and hd.hero=?2 group by hd.target order by hd.target " +
            "")
    List<HeroDamages> damage(Long matchId, String heroName);
}
