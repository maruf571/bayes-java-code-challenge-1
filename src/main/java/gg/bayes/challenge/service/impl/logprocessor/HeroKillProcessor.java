package gg.bayes.challenge.service.impl.logprocessor;

import gg.bayes.challenge.entity.HeroKill;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroKillRepository;
import gg.bayes.challenge.service.LogProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeroKillProcessor implements LogProcessor {

    @Getter
    private final List<HeroKill> heroKillList = new ArrayList<>();
    protected static final Pattern pattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+is+\\s+killed\\s+by\\s+npc_dota_hero_(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_TARGET_GROUP = 2;
    private static final int HERO_GROUP = 3;

    private final HeroKillRepository repository;

    @Override
    public void parse(String line, Match match) {

        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            HeroKill heroKill = new HeroKill();
            heroKill.setMatch(match);
            heroKill.setTarget(matcher.group(HERO_TARGET_GROUP));
            heroKill.setHero(matcher.group(HERO_GROUP));
            heroKill.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroKillList.add(heroKill);
        }
    }

    @Override
    public void save() {
        repository.saveAll(heroKillList);
        heroKillList.clear();
    }
}
