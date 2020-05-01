package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamages;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.List;

public interface MatchService {

    Long ingestMatch(String payload);

    List<HeroKills> getMatch(Long matchId);

    List<HeroItems> getHeroItems(Long matchId, String heroName);

    List<HeroSpells> getHeroSpells(Long matchId, String heroName);

    List<HeroDamages> getHeroDamage(Long matchId, String heroName);
}
