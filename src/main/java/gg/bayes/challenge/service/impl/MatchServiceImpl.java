package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.*;
import gg.bayes.challenge.exception.DotaException;
import gg.bayes.challenge.repository.*;
import gg.bayes.challenge.rest.model.HeroDamages;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.LogProcessorManager;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.service.MatchValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final HeroKillRepository heroKillRepository;
    private final HeroBuyItemRepository heroBuyItemRepository;
    private final HeroSpellRepository heroSpellRepository;
    private final HeroDamageRepository heroDamageRepository;
    private final MatchValidation matchValidation;
    private final LogProcessorManager logProcessorManager;

    @Override
    public List<HeroKills> getMatch(Long matchId) {
        matchValidation.validateMatch(matchId);
        return heroKillRepository.getHeroKills(matchId);
    }

    @Override
    public List<HeroItems> getHeroItems(Long matchId, String heroName) {
        matchValidation.validateMatch(matchId)
                .validateHeroName(heroName);
        return heroBuyItemRepository.heroItem(matchId, heroName);
    }

    @Override
    public List<HeroSpells> getHeroSpells(Long matchId, String heroName) {
        matchValidation.validateMatch(matchId)
                .validateHeroName(heroName);
        return heroSpellRepository.heroSpells(matchId, heroName);
    }

    @Override
    public List<HeroDamages> getHeroDamage(Long matchId, String heroName) {
        matchValidation.validateMatch(matchId)
                .validateHeroName(heroName);
        return heroDamageRepository.damage(matchId, heroName);
    }

    @Override
    public Long ingestMatch(String payload) {

        long start = System.currentTimeMillis();
        Match match = matchRepository.save(new Match());
        parseLogFile(payload, match);
        saveHeroActivity();
        long end = System.currentTimeMillis();
        log.info("{} ms is taken to parse the log file.", (end - start));
        return match.getId();
    }

    private void parseLogFile(String payload, Match match) {
        try (BufferedReader reader = new BufferedReader(new StringReader(payload))) {
            String line = reader.readLine();
            while (line != null) {
                logProcessorManager.parse(line, match);
                line = reader.readLine();
            }
        } catch (IOException exc) {
            log.error("Error on reading payload");
            throw new DotaException("Error while reading payload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveHeroActivity() {
        logProcessorManager.save();
    }
}
