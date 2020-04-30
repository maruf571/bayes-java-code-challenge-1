package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.BaseKill;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroKillRepository;
import gg.bayes.challenge.service.LogProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class KillProcessor extends LogProcessor {

    static final Pattern killPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+is+\\s+killed\\s+by\\s+npc_dota_hero_(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_TARGET_GROUP = 2;
    private static final int HERO_GROUP = 3;

    private final HeroKillRepository killRepository;

    @Override
    public boolean process(String line, Match match) {
        Matcher matcher = killPattern.matcher(line);
        if(matcher.find()) {
            BaseKill heroKill = new BaseKill();
            heroKill.setMatch(match);
            heroKill.setTarget(matcher.group(HERO_TARGET_GROUP));
            heroKill.setHero(matcher.group(HERO_GROUP));
            heroKill.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroKills.add(heroKill);
            return true;
        }
        return this.next.process(line, match);
    }

    @Override
    public void save() {
        killRepository.saveAll(heroKills);
        heroKills.clear();
        this.next.save();
    }
}
