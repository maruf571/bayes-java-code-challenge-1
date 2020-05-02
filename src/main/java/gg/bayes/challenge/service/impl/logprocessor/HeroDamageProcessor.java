package gg.bayes.challenge.service.impl.logprocessor;

import gg.bayes.challenge.entity.HeroDamage;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroDamageRepository;
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
public class HeroDamageProcessor implements LogProcessor {

    @Getter
    private final List<HeroDamage> heroDamageList = new ArrayList<>();
    protected static final Pattern pattern = Pattern.compile("\\[(.*)]\\snpc_dota_hero_(.*)\\shits\\snpc_dota_hero_(.*)\\swith\\s(.*)\\sfor\\s(.*)\\sdamage");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int HERO_TARGET_GROUP = 3;
    private static final int HIT_BY_GROUP = 4;
    private static final int DAMAGE_GROUP = 5;

    private final HeroDamageRepository repository;

    @Override
    public void parse(String line, Match match) {
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            HeroDamage heroDamage = new HeroDamage();
            heroDamage.setMatch(match);
            heroDamage.setHero(matcher.group(HERO_GROUP));
            heroDamage.setTarget(matcher.group(HERO_TARGET_GROUP));
            heroDamage.setDamage(Integer.valueOf(matcher.group(DAMAGE_GROUP)));
            heroDamage.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroDamageList.add(heroDamage);
        }
    }

    @Override
    public void save() {
        repository.saveAll(heroDamageList);
        heroDamageList.clear();
    }
}
