package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.HeroDamage;
import gg.bayes.challenge.entity.HeroKill;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.exception.DotaException;
import gg.bayes.challenge.repository.*;
import gg.bayes.challenge.rest.model.HeroDamages;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.LogProcessor;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private LogProcessor logProcessor;

    private final BuyProcessor buyProcessor;
    private final CastProcessor castProcessor;
    private final HitProcessor hitProcessor;
    private final KillProcessor killProcessor;
    private final UsesProcessor usesProcessor;
    private final MatchRepository matchRepository;

    private final HeroKillRepository heroKillRepository;
    private final HeroItemRepository heroItemRepository;
    private final HeroSpellRepository heroSpellRepository;
    private final HeroDamageRepository heroDamageRepository;

    @PostConstruct
    public void init() {
        this.logProcessor = buyProcessor;   // set first one
        logProcessor.setNext(castProcessor) // create chain
                .setNext(hitProcessor)
                .setNext(killProcessor)
                .setNext(usesProcessor);
    }

    @Override
    public Long ingestMatch(String payload) {

        long start = System.currentTimeMillis();
        Match match = matchRepository.save(new Match());
        try (BufferedReader reader = new BufferedReader(new StringReader(payload))) {
            String line = reader.readLine();
            while (line != null) {
                logProcessor.process(line, match);
                line = reader.readLine();
            }
        } catch (IOException exc) {
             log.error("Error on reading payload");
             throw new DotaException("Error while reading payload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        long end = System.currentTimeMillis();
        log.info("{} ms is taken to parse the log file.", (end - start));

        return match.getId();
    }

    @Override
    public List<HeroKills> getMatch(Long matchId) {
        return heroKillRepository.getHeroKills(matchId);
    }

    @Override
    public List<HeroItems> getItems(Long matchId, String heroName) {
        return heroItemRepository.heroItem(matchId, heroName);
    }

    @Override
    public List<HeroSpells> getSpells(Long matchId, String heroName) {
        return heroSpellRepository.heroSpells(matchId, heroName);
    }

    @Override
    public List<HeroDamages> getDamage(Long matchId, String heroName) {
        return heroDamageRepository.damage(matchId, heroName);
    }
}
