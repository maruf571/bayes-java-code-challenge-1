package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroSpellRepository extends JpaRepository<HeroSpell, Long> {

    @Query("SELECT new gg.bayes.challenge.rest.model.HeroSpells(spell.spell, count(spell) as casts)  " +
            "FROM HeroSpell as spell " +
            "where spell.match.id = ?1  and spell.hero = ?2 group by spell.spell order by spell.spell")
    List<HeroSpells> heroSpells(Long matchId, String heroName);
}
